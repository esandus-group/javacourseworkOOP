package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.Student;
import SCMS.Utils.SCMSEnvironment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MarkAttendanceController {
    CheckBox []checkBoxArray;
    public ArrayList<Student> allStudentsPresent = new ArrayList();
    ObservableList<Student> allTheStudents = FXCollections.observableArrayList();

    //=========================================================================
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //getting the connection
    @FXML
    private Label topic;
    @FXML
    private Button fillTable;
    private Club club;
    @FXML
    private Button backButtonMark;
    @FXML
    private Button submit;
    private String eventID;
    private int numOfStudents;
    @FXML
    private TableView<Student> allStudents;

    @FXML
    private TableColumn<Student, String> stdFnameCol;
    @FXML
    private TableColumn<Student, String> stdLnameCol;
    @FXML
    private TableColumn<Student, String> stdIdCol;
    @FXML
    private TableColumn<Student, String> tick;
    private String buttonText;

    //=========================================================================
    public void gettingInformation(Club club, String eventId){ //THE METHOD TO GET THE INFOR FROM THE PREVIOUS CONTROLLER
        this.club=club;
        this.eventID=eventId;
        topic.setText("marking attendance of "+club.getName());
        this.numOfStudents=club.getStudentsPresent().size();
        checkBoxArray = new CheckBox[numOfStudents];
    }
    //=========================================================================
    public ArrayList<Student> getStudentsForClub(String clubId) { //WE SHOULD PASS THE EVENT ID ALSO AND CHECK WHERE THERE IS A RECORD IN ATTENDANCE TABLE WITH THAT ID AND EVENTID
        ArrayList<Student> clubStudents = new ArrayList<>();

        // Assuming Club_Student table has columns clubId and studentId
        String clubStudentQuery = "SELECT id FROM Club_Student WHERE clubId = ?";
        String studentQuery = "SELECT * FROM Student WHERE id = ?";

        try (PreparedStatement clubStudentStatement = connections.prepareStatement(clubStudentQuery)) {
            clubStudentStatement.setString(1, clubId);
            ResultSet clubStudentResultSet = clubStudentStatement.executeQuery();

            while (clubStudentResultSet.next()) {
                String studentId = clubStudentResultSet.getString("id");

                try (PreparedStatement studentStatement = connections.prepareStatement(studentQuery)) {
                    studentStatement.setString(1, studentId);
                    ResultSet studentResultSet = studentStatement.executeQuery();

                    if (studentResultSet.next()) {
                        String id = studentResultSet.getString("id");
                        String firstName = studentResultSet.getString("firstName");
                        String lastName = studentResultSet.getString("lastName");
                        String dateOfBirth = studentResultSet.getString("dateOfBirth");
                        String password = studentResultSet.getString("password");

                        // Assuming you have checkBoxArray already declared and initialized
                        CheckBox checkBox = new CheckBox();
                        checkBox.setUserData(studentId); // Set an identifier (can be any unique value)


                        Student student = new Student(id, firstName, lastName, dateOfBirth, password, checkBox);
                        clubStudents.add(student);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clubStudents;
    }
    //=========================================================================
    public void loadingStudents() {
        stdIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        stdFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        stdLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tick.setCellValueFactory(new PropertyValueFactory<>("checkBox"));


        ObservableList<Student> data;
        data = allTheStudents; //this is the observable list
        data.addAll(allStudentsPresent);
        allStudents.setItems(data); //this is the tableview
    }
    //=========================================================================
    public void onFillTableClick(ActionEvent event) throws Exception{
        allStudentsPresent = getStudentsForClub(club.getClubId());

        loadingStudents();
        fillTable.setDisable(true);

    }
    //=========================================================================
    public ArrayList selectedRows(ObservableList<Student> data){ //getting the selected rows (students)
        ArrayList<Student> studentsClicked = new ArrayList<>();
        for (Student e : data){
            if (e.getCheckBox().isSelected()){
                studentsClicked.add(e);
            }
        }
        return studentsClicked;
    }
    //=========================================================================
    public void  backButtonMark  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        buttonText = club.getName();

        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText,club.getClubId());

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //=========================================================================
    public void submit(ActionEvent event) throws Exception {
        ArrayList<Student> studentsWhoCame = selectedRows(allTheStudents); //getting the students who are present to a arraylist

        saveAttendance(studentsWhoCame, eventID);
        System.out.println("Attendance saved successfully!");

        //AFTER SUBMITTING IT SHOULD GO BACK TO THE PREVIOUS PAGE, THIS IS THAT CODE
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        buttonText = club.getName();

        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText,club.getClubId());

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    //=========================================================================
    public void saveAttendance(ArrayList<Student> students, String eventId) {
        String selectQuery = "SELECT COUNT(*) FROM Attendance WHERE eventId = ? AND id = ?";
        String insertQuery = "INSERT INTO Attendance (eventId, id) VALUES (?, ?)";

        try (PreparedStatement selectStatement = connections.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connections.prepareStatement(insertQuery)) {

            for (Student student : students) {
                // Check if the student already has attendance for the event
                selectStatement.setString(1, eventId);
                selectStatement.setString(2, student.getId());

                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    // If the student is not present, add the attendance record
                    if (count == 0) {
                        insertStatement.setString(1, eventId);
                        insertStatement.setString(2, student.getId());
                        insertStatement.addBatch(); // Add the statement to the batch for batch processing
                    }
                }
            }

            insertStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

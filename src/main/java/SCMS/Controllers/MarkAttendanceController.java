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
    CheckBox []checkBoxArray; //declares an array of CheckBox objects named checkBoxArray.
    public ArrayList<Student> allStudentsPresent = new ArrayList(); //to store the objects of the Student class
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
        //updates the Label named topic with a string indicating the club's name for marking attendance.
        topic.setText("marking attendance of "+club.getName());
        this.numOfStudents=club.getStudentsPresent().size();//retrieving the size of students present in the club.
        checkBoxArray = new CheckBox[numOfStudents];//initializes the checkBoxArray to hold CheckBox
        // objects based on the number of students present.
    }
    //=========================================================================
    public ArrayList<Student> getStudentsForClub(String clubId) { //WE SHOULD PASS THE EVENT ID ALSO AND CHECK WHERE THERE IS A RECORD IN ATTENDANCE TABLE WITH THAT ID AND EVENTID
        //retrieve students associated with a specific club from the database.
        ArrayList<Student> clubStudents = new ArrayList<>();
        //store the retrieved students associated with the given club.

        // Assuming Club_Student table has columns clubId and studentId
        String clubStudentQuery = "SELECT id FROM Club_Student WHERE clubId = ?";
        //retrieve student IDs from a table named Club_Student based on the provided clubId.
        String studentQuery = "SELECT * FROM Student WHERE id = ?";
        //retrieve detailed student information from a table named Student based on the obtained student IDs.

        try (PreparedStatement clubStudentStatement = connections.prepareStatement(clubStudentQuery)) {
            //executes a SQL statement to fetch student IDs associated with the given clubId.
            clubStudentStatement.setString(1, clubId);
            //The resulting set is stored in clubStudentResultSet.
            ResultSet clubStudentResultSet = clubStudentStatement.executeQuery();

            while (clubStudentResultSet.next()) { //it iterates through the result set of student IDs retrieved for the club
                String studentId = clubStudentResultSet.getString("id");
                //It retrieves each student ID and stores it in the studentId variable.

                try (PreparedStatement studentStatement = connections.prepareStatement(studentQuery)) {
                    // used to fetch detailed information for each student using their respective studentId.
                    studentStatement.setString(1, studentId);
                    //sets the studentId parameter and retrieves a result set containing student details.
                    ResultSet studentResultSet = studentStatement.executeQuery();

                    if (studentResultSet.next()) {
                        //// Retrieving various attributes of the student from the result set
                        String id = studentResultSet.getString("id");
                        String firstName = studentResultSet.getString("firstName");
                        String lastName = studentResultSet.getString("lastName");
                        String dateOfBirth = studentResultSet.getString("dateOfBirth");
                        String password = studentResultSet.getString("password");

                        // Assuming you have checkBoxArray already declared and initialized
                        // Create a CheckBox for the student and set a unique identifier (studentId)
                        CheckBox checkBox = new CheckBox();
                        checkBox.setUserData(studentId); // Set an identifier (can be any unique value)

                        // Create a Student object with retrieved information and the CheckBox
                        Student student = new Student(id, firstName, lastName, dateOfBirth, password, checkBox);
                        // Add the student to the list of clubStudents
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
    //populating a TableView with student data.
    public void loadingStudents() {
        // specifies which properties of the Student class should be used to populate each column.
        stdIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        stdFnameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        stdLnameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tick.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        //PropertyValueFactory is used to link the properties of the Student class
        // (such as id, firstName, lastName, and checkBox) to the respective columns in the TableView.


        ObservableList<Student> data; //This declares an ObservableList of Student objects named data
        data = allTheStudents; //this is the observable list
        //assigns it the reference of the allTheStudents ObservableList.
        data.addAll(allStudentsPresent);
        //This line adds all the elements from the allStudentsPresent ArrayList
        // (which presumably contains Student objects) to the data ObservableList.
        allStudents.setItems(data); // sets the items of the TableView named allStudents
        // with the data from the data ObservableList.
    }
    //=========================================================================
    //this is the method when the fill table button is clicked
    public void onFillTableClick(ActionEvent event) throws Exception{
        allStudentsPresent = getStudentsForClub(club.getClubId());
        //method to retrieve students associated with the current club ID (club.getClubId()).
        //ArrayList with the retrieved students.
        loadingStudents();
        // update the TableView to display the loaded student data.
        //Sets up the TableView's columns and populates it with the student data
        // obtained from allStudentsPresent.
        fillTable.setDisable(true);
        //Disables the button named fillTable after the data has been loaded into the TableView.

    }
    //=========================================================================
    //aims to retrieve selected rows (students) from the TableView's data.
    public ArrayList selectedRows(ObservableList<Student> data){
        //Initializes an ArrayList named studentsClicked to store selected students.
        ArrayList<Student> studentsClicked = new ArrayList<>();
        //Iterates through each Student object in the provided data list, students in the table view
        for (Student e : data){
            //Checks if the checkbox associated with the current student (e) is selected in the TableView.
            if (e.getCheckBox().isSelected()){
                //Adds the selected student (e) to the studentsClicked list if its
                // associated checkbox is selected in the TableView.
                studentsClicked.add(e);
            }
        }
        return studentsClicked;////returns the list of selected students
    }
    //=========================================================================
    //triggered when the back button is clicked
    public void  backButtonMark  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        // load the FXML file specified by fileName and initialize its contents.
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
    //this is the method when the submit button is clicked
    public void submit(ActionEvent event) throws Exception {
        ArrayList<Student> studentsWhoCame = selectedRows(allTheStudents); //getting the students who are present to a arraylist

        saveAttendance(studentsWhoCame, eventID);//saving data for the students who are present
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
    //save attendance records for students related to a particular event.
    public void saveAttendance(ArrayList<Student> students, String eventId) {
        //selectQuery is a SQL query that counts the number of attendance records for a
        // specific eventId and id in the Attendance table.
        String selectQuery = "SELECT COUNT(*) FROM Attendance WHERE eventId = ? AND id = ?";
        //insertQuery is a SQL query used to insert attendance records into the Attendance table
        // for a specific eventId and id.
        String insertQuery = "INSERT INTO Attendance (eventId, id) VALUES (?, ?)";

        try (PreparedStatement selectStatement = connections.prepareStatement(selectQuery);
             PreparedStatement insertStatement = connections.prepareStatement(insertQuery)) {

            //For each student, it sets parameters (eventId and student.getId()) in the
            // selectStatement to check whether an attendance record exists for that specific student
            // and event.
            for (Student student : students) {
                // Check if the student already has attendance for the event
                selectStatement.setString(1, eventId);
                selectStatement.setString(2, student.getId());

                //retrieve the count of attendance records for the specific student and event.
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    // If the student is not present, add the attendance record
                    if (count == 0) {
                        // indicating that the student doesn't have an attendance record for the event
                        //prepares an insertStatement to add the attendance record for that
                        // student and event into a batch for batch processing.
                        insertStatement.setString(1, eventId);
                        insertStatement.setString(2, student.getId());
                        insertStatement.addBatch(); // Add the statement to the batch for
                        // batch processing
                    }
                }
            }

            insertStatement.executeBatch(); //execute the batched insertion of attendance records into the database.
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

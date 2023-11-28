package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.ClubAdvisor;
import SCMS.Objects.Event;
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
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MarkAttendanceController {     //(FULLY DONE BY NIKOYA)
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
    private ClubAdvisor advisor;
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

    private Event function;
    private String buttonText;
    public Event getEventById(String eventId) throws SQLException {
        String eventQuery = "SELECT * FROM Event WHERE eventId = ?";
        String attendanceQuery = "SELECT * FROM Attendance WHERE eventId = ?";
        String studentQuery = "SELECT * FROM Student WHERE id = ?";

        Event event = null;

        try (PreparedStatement eventStatement = connections.prepareStatement(eventQuery)) {
            eventStatement.setString(1, eventId);
            ResultSet eventResult = eventStatement.executeQuery();

            if (eventResult.next()) {
                String title = eventResult.getString("title");
                LocalDateTime dateTime = eventResult.getObject("dateTime", LocalDateTime.class);
                String venue = eventResult.getString("venue");
                String type = eventResult.getString("typeOfClubFunction");
                String description = eventResult.getString("description");

                // Created a list to hold students who joined the event
                ArrayList<Student> studentsWhoJoined = new ArrayList<>();

                // Retrieve students who joined the event from the Attendance table
                try (PreparedStatement attendanceStatement = connections.prepareStatement(attendanceQuery)) {
                    attendanceStatement.setString(1, eventId);
                    ResultSet attendanceResult = attendanceStatement.executeQuery();

                    while (attendanceResult.next()) {
                        String studentId = attendanceResult.getString("id");

                        // Fetching the student objects using the studentId and add to the list
                        try (PreparedStatement studentStatement = connections.prepareStatement(studentQuery)) {
                            studentStatement.setString(1, studentId);
                            ResultSet studentResult = studentStatement.executeQuery();

                            if (studentResult.next()) {
                                String firstName = studentResult.getString("firstName");
                                String lastName = studentResult.getString("lastName");
                                String dateOfBirth = studentResult.getString("dateOfBirth");
                                String password = studentResult.getString("password");

                                Student student = new Student(studentId, firstName, lastName, dateOfBirth, password);
                                studentsWhoJoined.add(student);
                            }
                        }
                    }
                }

                // Creating the Event object with the retrieved data
                event = new Event(eventId, title, dateTime, venue, type, description, new Button(), studentsWhoJoined);
            }
        }

        return event;
    }
    //=========================================================================
    public void gettingInformation(Club club, String eventId, ClubAdvisor advisor){ //THE METHOD TO GET THE INFOR FROM THE PREVIOUS CONTROLLER
        this.club=club;
        this.advisor=advisor;
        this.eventID=eventId;
        //updates the Label named topic with a string indicating the club's name for marking attendance.
        topic.setText("marking attendance of "+club.getName());
        this.numOfStudents=club.getStudentsPresent().size();//retrieving the size of students present in the club.
        checkBoxArray = new CheckBox[numOfStudents];//initializes the checkBoxArray to hold CheckBox
        // objects based on the number of students present.
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
    public void onFillTableClick(ActionEvent event) throws Exception{           //3.3.1  calling the method to open the students present
        allStudentsPresent = club.getStudentsPresent();    //2.  calling the method to getStudentsPresent

        //method to retrieve students associated with the current club ID (club.getClubId()).
        //ArrayList with the retrieved students.

        loadingStudents();      //3.  calling the method to load the data to the table

        // update the TableView to display the loaded student data.
        //Sets up the TableView's columns and populates it with the student data
        // obtained from allStudentsPresent.
        fillTable.setDisable(true);
        //Disables the button named fillTable after the data has been loaded into the TableView.

    }
    //=========================================================================
    //aims to retrieve selected rows (students) from the TableView's data.
    public ArrayList getSelectedRows(ObservableList<Student> data){              //3.3.2.1  calling the method to get the selected rows
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
        pcc.gettingAdvisorFromMarkAttendanceCon(advisor);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //=========================================================================
    //this is the method when the submit button is clicked
    public void submit(ActionEvent event) throws Exception {     //3.3.2.  calling the method to submit

        function = getEventById(eventID);
        function.setStudentsWhoJoined(getSelectedRows(allTheStudents)); //getting the students who are present to a arraylist

        saveAttendance(function);//saving data for the students who are present
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
    public void saveAttendance(Event function) {           //3.3.3  calling the method to save to the database
        ArrayList<Student> students = function.getStudentsWhoJoined();
        String eventId = function.getEventId();

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

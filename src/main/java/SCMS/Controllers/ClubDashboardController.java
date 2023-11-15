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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import SCMS.Objects.Event;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ClubDashboardController {
    public ArrayList<Event> functionsPresent = new ArrayList<>();
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB
    @FXML
    private TableView<Event> functionsTable;
    @FXML
    private TableColumn<Event, String> funcTypeCol;
    @FXML
    private TableColumn<Event, String> titleCol;
    @FXML
    private TableColumn<Event, String> datenTimeCol;
    @FXML
    private TableColumn<Event, String> venueCol;
    private Student student;
    @FXML
    private Text clubNameText;
    String clubName;
    String stdName;
    String stdId;
    public void InitializeStudent(String clubName, Student std){
        this.student = std;
        this.clubName = clubName;
        this.stdName = student.getFirstName();
        clubNameText.setText(clubName);
        functionsPresent = loadDataToEventTable();
    }
    public void loadingStudents() {
        funcTypeCol.setCellValueFactory(new PropertyValueFactory<Event, String>("Type"));
        titleCol.setCellValueFactory(new PropertyValueFactory<Event, String>("Title"));
        datenTimeCol.setCellValueFactory(new PropertyValueFactory<Event, String>("DateTime"));
        venueCol.setCellValueFactory(new PropertyValueFactory<Event, String>("Venue"));

        ObservableList<Event> data = FXCollections.observableArrayList();
        data.addAll(functionsPresent);
        functionsTable.setItems(data);
    }


    public ArrayList<Event> loadDataToEventTable() {
        ArrayList<Event> functions = new ArrayList<>();
        try {
            Statement st = connections.createStatement();
            // First, let's retrieve the club ID based on the club name
            String clubIdQuery = "SELECT clubId FROM club WHERE name = '" + clubName + "'";
            ResultSet clubIdResult = st.executeQuery(clubIdQuery);

            if (clubIdResult.next()) {
                String clubId = clubIdResult.getString("clubId");

                // Now, use the club ID to retrieve the event details
                String query = "SELECT typeOfClubFunction, title, dateTime, venue FROM event WHERE clubId = '" + clubId + "'";
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    // Create EventData objects and add them to the list
                    String functionType = rs.getString("typeOfClubFunction");
                    String title = rs.getString("title");
                    String dateTimeString = rs.getString("dateTime");
                    String venue = rs.getString("venue");
                    LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    functions.add(new Event(functionType, title, dateTime, venue));

                }
            }

            clubIdResult.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return functions;
    }

    public void onLeaveClubButtonClick(ActionEvent event) throws Exception {
        try {
            String stdId = getStudentIdByName(stdName); // Get student ID by name

            if (stdId != null) {
                String clubId = getClubIdByName(clubName); // Get club ID by name

                if (clubId != null) {
                    deleteClubMembership(stdId, clubId);

                    // Update the student object
                    student.leaveClub(getClubByName(clubName));

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Load StudentDashboard
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
        Parent root = loader.load();
        StudentDashboardController SDC = loader.getController();
        SDC.InitializeStudent(student);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private String getStudentIdByName(String studentName) throws SQLException {
        String stdId = null;

        try (PreparedStatement stdIdStatement = connections.prepareStatement("SELECT id FROM student WHERE firstName = ?")) {
            stdIdStatement.setString(1, studentName);
            ResultSet stdIdResult = stdIdStatement.executeQuery();

            if (stdIdResult.next()) {
                stdId = stdIdResult.getString("id");
            }
        }

        return stdId;
    }

    private String getClubIdByName(String clubName) throws SQLException {
        String clubId = null;

        try (PreparedStatement clubIdStatement = connections.prepareStatement("SELECT clubId FROM club WHERE name = ?")) {
            clubIdStatement.setString(1, clubName);
            ResultSet clubIdResult = clubIdStatement.executeQuery();

            if (clubIdResult.next()) {
                clubId = clubIdResult.getString("clubId");
            }
        }

        return clubId;
    }

    private void deleteClubMembership(String studentId, String clubId) throws SQLException {
        try (PreparedStatement deleteStatement = connections.prepareStatement("DELETE FROM club_student WHERE clubId = ? AND id = ?")) {
            deleteStatement.setString(1, clubId);
            deleteStatement.setString(2, studentId);
            deleteStatement.executeUpdate();
        }
    }
    public Student getStudent(String studentId) {
        String query = "SELECT * FROM Student WHERE id = ?";
        Student student = null;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, studentId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve student details from the result set
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String dateOfBirth = resultSet.getString("dateOfBirth");
                String password =  resultSet.getString("password");
                // Add more fields as needed

                // Create a Student object with the retrieved data
                student = new Student(id, firstName, lastName, dateOfBirth,password);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return student;
    }
    public Club getClubByName(String clubName) throws SQLException {
        String clubQuery = "SELECT * FROM Club WHERE name = ?";
        String clubStudentQuery = "SELECT * FROM Club_Student WHERE clubId = ?";

        Club club = null;

        // Retrieve club details from the Club table using the club name
        try (PreparedStatement clubStatement = connections.prepareStatement(clubQuery)) {
            clubStatement.setString(1, clubName);
            ResultSet clubResult = clubStatement.executeQuery();

            if (clubResult.next()) {
                String clubId = clubResult.getString("clubId"); // Get the ID of the club
                String name = clubResult.getString("name");
                String idOfAdvisor = clubResult.getString("idOfAdvisor");

                // Create a list to hold students present
                ArrayList<Student> studentsPresent = new ArrayList<>();

                // Retrieve student IDs associated with the club from the Club_Student table
                try (PreparedStatement clubStudentStatement = connections.prepareStatement(clubStudentQuery)) {
                    clubStudentStatement.setString(1, clubId);
                    ResultSet clubStudentResult = clubStudentStatement.executeQuery();

                    while (clubStudentResult.next()) {
                        String studentId = clubStudentResult.getString("id");
                        // Fetch student objects using the studentId and add to the list
                        Student student = getStudent(studentId);
                        if (student != null) {
                            studentsPresent.add(student);
                        }
                    }
                }

                // Create the Club object with the retrieved data
                club = new Club(clubId, name, idOfAdvisor, studentsPresent);
            }
        }

        return club;
    }

}

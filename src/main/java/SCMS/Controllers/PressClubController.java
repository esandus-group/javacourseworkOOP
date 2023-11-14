package SCMS.Controllers;

import SCMS.HelloApplication;
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

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PressClubController {
    HelloApplication helloApplicationInstance = new HelloApplication();
    @FXML
    private TextField newAdvisorId;
    @FXML
    private TableView<Event> onComingEvents;
    public String confirmation;
    public String advisorIdOfNewPerson;

    @FXML
    private Label deletingStatus;

    @FXML
    private Label deletingStatus1;
    @FXML
    private Button deleteClub;

    @FXML
    private Button veiwStudentsButton;

    @FXML
    private TextField confirmText;
    @FXML
    private TextField advisorId;

    String currentAdvisorId;

    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private TableColumn<Event, String> colAttendance;

    @FXML
    private TableColumn<Event, String> colDate;

    @FXML
    private TableColumn<Event, String> colFunction;
    @FXML
    private TableColumn<Event, String> colType;

    @FXML
    private TableColumn<Event, String> colVenue;

    @FXML
    private Button removeStudent;

    @FXML
    private Button fillTable;

    @FXML
    private Button createNewEvent;
    @FXML
    private Button backButtonCDD;

    public ArrayList<Event> allEvents = new ArrayList();
    Stage stage;
    ClubAdvisor currentClubAdvisor = null;

    ClubAdvisor newClubAdvisor = null;

    Club currentClub = null;

    String clubidddd= "C1";
//=======================================================

    public void loadingEvents() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colFunction.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("button"));

        ObservableList<Event> data = FXCollections.observableArrayList();
        data.addAll(allEvents);
        onComingEvents.setItems(data);
    }

    public ArrayList<Event> getEventsForClub(String clubId) {
        ArrayList<Event> clubEvents = new ArrayList<>();
        String query = "SELECT * FROM Event WHERE clubId = ?"; // Make sure the table name is correct

        try (PreparedStatement preparedStatement = connections.prepareStatement(query)) {
            preparedStatement.setString(1, clubId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String eventId = resultSet.getString("eventId");
                String title = resultSet.getString("title");
                LocalDateTime dateTime = resultSet.getTimestamp("dateTime").toLocalDateTime();
                String venue = resultSet.getString("venue");
                String typeOfClubFunction = resultSet.getString("typeOfClubFunction");
                String description = resultSet.getString("description");
                String retrievedClubId = resultSet.getString("clubId");

                Event event = new Event(title, dateTime, venue, typeOfClubFunction, description, retrievedClubId);
                clubEvents.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return clubEvents;
    }

    public void onFillTableClick(ActionEvent event) throws Exception{
        allEvents = getEventsForClub(clubidddd);

        loadingEvents();
        fillTable.setDisable(true);
    }
    public void stageLoader(ActionEvent event, String fileName) throws IOException { //STAGE LOADER METHOD
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/Club advisor.fxml";      //open the page
        stageLoader(event,fileName);
    }
    //=============================================================================
    public void onRemoveStudentClick(ActionEvent event) throws Exception{
        String fileName="/SCMS/FxmlFiles/DeleteStudent.fxml";      //open the page
        stageLoader(event,fileName);

    }
    //===========================================================================
    public void onViewStudentsButtonClick(ActionEvent event) throws Exception{
        String fileName="/SCMS/FxmlFiles/view Students.fxml";      //open the page
        stageLoader(event,fileName);

    }
    //=============================================================================
    public boolean checkIfAdvisorManagesClub(String clubId, String advisorId) {
        String query = "SELECT 1 FROM Club WHERE clubId = ? AND idOfAdvisor = ?";

        boolean advisorManagesClub = false;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, clubId);
            statement.setString(2, advisorId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                advisorManagesClub = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return advisorManagesClub;
    }
    //make the method to replace the advisor id with the new advisor id in the club table

    //-------------------------------------------------------------------------------------
    public ClubAdvisor getClubAdvisor(String clubAdvisorId) throws SQLException {
        String advisorQuery = "SELECT * FROM ClubAdvisor WHERE id = ?";
        String clubsQuery = "SELECT * FROM Club WHERE idOfAdvisor = ?";

        try (PreparedStatement advisorStatement = this.connections.prepareStatement(advisorQuery);
             PreparedStatement clubsStatement = this.connections.prepareStatement(clubsQuery)) {
            advisorStatement.setString(1, clubAdvisorId);
            clubsStatement.setString(1, clubAdvisorId);

            ResultSet advisorResultSet = advisorStatement.executeQuery();
            ResultSet clubsResultSet = clubsStatement.executeQuery();

            if (advisorResultSet.next()) {
                String id = advisorResultSet.getString("id");
                String firstName = advisorResultSet.getString("firstName");
                String lastName = advisorResultSet.getString("lastName");
                String dateOfBirth = advisorResultSet.getString("dateOfBirth");
                String password = advisorResultSet.getString("password");

                ArrayList<Club> managingClubs = new ArrayList<>();
                while (clubsResultSet.next()) {
                    String clubId = clubsResultSet.getString("clubId");
                    String clubName = clubsResultSet.getString("name");

                    // Create a Club object and add it to the managingClubs list
                    Club club = new Club(clubId,clubName,id);
                    managingClubs.add(club);
                }

                return new ClubAdvisor(id, firstName, lastName, dateOfBirth, password, managingClubs);
            }
        }

        return null; // ClubAdvisor not found
    }

    //=================================================================
    public void onAssignNewAdvClick(ActionEvent event) throws Exception { //using the advisorID create the object
        confirmation = confirmText.getText();                          //getting the stuff from the text fields
        advisorIdOfNewPerson = newAdvisorId.getText(); //this should be the advisors id
        currentAdvisorId =advisorId.getText();
        //get
        if (confirmation.equals("CONFIRM")) {

            // Checking if the advisor manages the club
            if (!checkIfAdvisorManagesClub(advisorIdOfNewPerson, currentAdvisorId)) {

                deletingStatus.setText("An advisor with That ID does not manage a Club");
                deletingStatus1.setText("Club not deleted");

            } else {
                currentClubAdvisor = getClubAdvisor(currentAdvisorId);
                newClubAdvisor = getClubAdvisor(advisorIdOfNewPerson);
                //currentClub=getClubByName();  FIX this line , then done  also save to database


                boolean status = currentClubAdvisor.assignNewAdvisor(newClubAdvisor,currentClub);
                 //pass the new burgers id and the club object

                if (status){
                    updateClubAdvisor(currentClub,newClubAdvisor);
                    //now i have to load him back to his dashboard cause the club is not his to do shit
                    //clearing the text fields
                    confirmText.clear();
                    newAdvisorId.clear();
                    advisorId.clear();
                }


            }
        } else {
            deletingStatus1.setText("Action not confirmed,advisor not assigned.");
        }
    }

    //=======================================================
    public void onCreateNewEvent(ActionEvent event) throws IOException {
        helloApplicationInstance.stageLoader(event, "/SCMS/FxmlFiles/Event.fxml");
    }
    //=========================================================================

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
    //=======================================================
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
    public boolean updateClubAdvisor(Club currentClub, ClubAdvisor newClubAdvisor) { //i can make it so that i pass the objects and then use getters
        String newAdvisorId = newClubAdvisor.getId();
        String clubId = currentClub.getClubId();

        String updateQuery = "UPDATE Club SET idOfAdvisor = ? WHERE clubId = ?";

        try (PreparedStatement preparedStatement = connections.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newAdvisorId);
            preparedStatement.setString(2, clubId);

            int rowsUpdated = preparedStatement.executeUpdate();

            return rowsUpdated > 0; // Checking  if any rows were updated, if so then correct
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }


    }



}



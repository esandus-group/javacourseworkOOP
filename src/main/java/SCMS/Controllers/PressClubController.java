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
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB
    HelloApplication helloApplicationInstance = new HelloApplication();
    @FXML
    private TextField newAdvisorId;
    @FXML
    private TableView<Event> onComingEvents;
    public String confirmation;
    public String advisorIdOfNewPerson;
    @FXML
    private Label welcomeClub;

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
    @FXML
    private Button generateReportsButton;

    ClubAdvisor currentClubAdvisor = null;
    ClubAdvisor newClubAdvisor = null;
    Club currentClub = null;
    String advisorID;
    String name;
    Button []button = new Button [3]; //here
    public ArrayList<Event> allEvents = new ArrayList();
    ObservableList<Event> allTheEvents = FXCollections.observableArrayList();
    public void openingEventStudentList(ActionEvent event,int buttonId) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/M123.fxml"));
        Parent root = loader.load();

        MarkAttendanceController mac = loader.getController();
        mac.gettingInformation(getClubByName(name), allEvents.get(buttonId).getEventId());

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void handleMarkAttendanceAction(ActionEvent event) throws IOException, SQLException {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            int buttonId = (int) clickedButton.getUserData();
            System.out.println("Button clicked: " + buttonId);

            // Open another FXML file or perform any action based on the buttonId
            if (buttonId == 0) {
                openingEventStudentList(event,buttonId);

            } else if (buttonId == 1) {
                openingEventStudentList(event,buttonId);

            } else if (buttonId == 2) {
                openingEventStudentList(event,buttonId);

            }
        }
    }
    //=======================================================
    public void setWelcomeText(String clubName,String id){
        welcomeClub.setText("Welcome to "+clubName);
        this.name=clubName;
        this.advisorID=id;
    }
    //=========================================================================

    public void loadingEvents() {
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        colFunction.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("eventId"));
        colVenue.setCellValueFactory(new PropertyValueFactory<>("venue"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("button"));


        ObservableList<Event> data ;
        data = allTheEvents;
        data.addAll(allEvents);
        onComingEvents.setItems(data);
    }
    //=========================================================================

    public ArrayList<Event> getEventsForClub(String clubId) {
        ArrayList<Event> clubEvents = new ArrayList<>();
        String query = "SELECT * FROM Event WHERE clubId = ?";

        try (PreparedStatement preparedStatement = connections.prepareStatement(query)) {
            preparedStatement.setString(1, clubId);
            ResultSet resultSet = preparedStatement.executeQuery();

            for (int i = 0; i < button.length; i++) { //initializes buttons and attach actions to them
                button[i] = new Button();
                button[i].setUserData(i); // Set an identifier (can be any unique value)
                button[i].setOnAction(event -> {
                    try {
                        handleMarkAttendanceAction(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            int h = 0;
            while (resultSet.next()) { //the loop iterates through the result set
                // and data from the result set is retrieved for each event row and create event object
                String id = resultSet.getString("eventId");
                String title = resultSet.getString("title");
                LocalDateTime dateTime = resultSet.getTimestamp("dateTime").toLocalDateTime();
                String venue = resultSet.getString("venue");
                String typeOfClubFunction = resultSet.getString("typeOfClubFunction");
                String description = resultSet.getString("description");
                String retrievedClubId = resultSet.getString("clubId");

                Event event = new Event(id,title, dateTime, venue, typeOfClubFunction, description, retrievedClubId, button[h]);
                clubEvents.add(event);//event object added to clubEvents arraylist
                h++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clubEvents;
    }
//=====================================================================

    public void onFillTableClick(ActionEvent event) throws Exception{
        allEvents = getEventsForClub(getClubByName(name).getClubId()); //PUTTING THE STUFF TO THE TABLE
        System.out.println(allEvents.size());    // TO SEE WHETHER ALL THE STUFF ARE LOADED
        loadingEvents();                           // PUTTING IT TO THE TABLE
        fillTable.setDisable(true);
    }

    //=========================================================================
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
        Parent root = loader.load();
        clubAdvisorController cac = loader.getController();
        cac.setWelcomeText(getClubAdvisor(advisorID).getFirstName(),getClubAdvisor(advisorID));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    //=============================================================================
    public void onRemoveStudentClick(ActionEvent event) throws Exception{
        String fileName="/SCMS/FxmlFiles/DeleteStudent.fxml";      //open the page
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();

        RemoveStudentController rsc = loader.getController();
        rsc.gettingInformation(getClubByName(name),advisorID);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    //===========================================================================
    public void onViewStudentsButtonClick(ActionEvent event) throws Exception{
        String fileName="/SCMS/FxmlFiles/View Students.fxml";      //the fxml path
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();

        ViewStudentsController vsc = loader.getController();
        vsc.gettingInformation(getClubByName(name),advisorID);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    //================================================================================
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
                    Club club = new Club(clubId,clubName);
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

        //get confirmation
        if (confirmation.equals("CONFIRM")) {
            currentClubAdvisor = getClubAdvisor(advisorID);
            newClubAdvisor = getClubAdvisor(advisorIdOfNewPerson);
            currentClub=getClubByName(name);

            // Checking if the advisor manages the club
            if (newClubAdvisor ==null) {

                deletingStatus.setText("An advisor with That ID does not Exist");
                deletingStatus1.setText("Club not deleted");

            } else {

                boolean status = currentClubAdvisor.assignNewAdvisor(newClubAdvisor,currentClub);
                System.out.println(status);
                 //pass the new burgers id and the club object

                if (status){
                    updateClubAdvisor(currentClub,newClubAdvisor);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
                    Parent root = loader.load();
                    clubAdvisorController cac = loader.getController();
                    cac.setWelcomeText(currentClubAdvisor.getFirstName(),currentClubAdvisor);
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                }
                else{
                    deletingStatus1.setText("That advisor already has 4 clubs");
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
                club = new Club(clubId, name, studentsPresent);
            }
        }

        return club;
    }
    //=========================================================================

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



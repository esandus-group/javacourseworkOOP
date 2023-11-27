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
    //=====================================================================
    public Button []button=  new Button[7];

    public ArrayList<Event> allEvents = new ArrayList();
    ObservableList<Event> allTheEvents = FXCollections.observableArrayList();
    //=====================================================================

    public void gettingAdvisorFromMarkAttendanceCon(ClubAdvisor advisor){
        this.currentClubAdvisor=advisor;
        this.advisorID=currentClubAdvisor.getId();
    }
    //=====================================================================
    public void openingEventStudentList(ActionEvent event,int buttonId) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/M123.fxml"));      //3.3  calling the method to open the students present
        Parent root = loader.load();

        MarkAttendanceController mac = loader.getController();
        mac.gettingInformation(getClubByName(name), allEvents.get(buttonId).getEventId(),getClubAdvisor(advisorID));

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //=====================================================================
    public void handleMarkAttendanceAction(ActionEvent event) throws IOException, SQLException {    //3.1  calling the handle button actions method
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            int buttonId = (int) clickedButton.getUserData();
            System.out.println("Button clicked: " + buttonId);

            // Open another FXML file or perform any action based on the buttonId
            if (buttonId == 0) {
                openingEventStudentList(event,buttonId);             //3.2  calling the method to open the students present

            } else if (buttonId == 1) {
                openingEventStudentList(event,buttonId);

            } else if (buttonId == 2) {
                openingEventStudentList(event,buttonId);
            } else if (buttonId == 3) {
                openingEventStudentList(event,buttonId);
            } else if (buttonId == 4) {
                openingEventStudentList(event,buttonId);
            } else if (buttonId == 5) {
                openingEventStudentList(event,buttonId);
            } else if (buttonId == 6) {
                openingEventStudentList(event,buttonId);

            } else if (buttonId == 7) {
            openingEventStudentList(event,buttonId);

        }

        }
    }
    //=======================================================
    public void setWelcomeText(String clubName,String id) throws SQLException {
        welcomeClub.setText("Welcome to "+clubName);
        this.name=clubName;
        this.advisorID=id;

        System.out.println(advisorID);


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
    public Club getClubByName(String clubName) throws SQLException {
        String clubQuery = "SELECT * FROM Club WHERE name = ?";
        String clubStudentQuery = "SELECT * FROM Club_Student WHERE clubId = ?";
        String eventQuery = "SELECT * FROM Event WHERE clubId = ?";

        Club club = null;

        for (int i = 0; i < button.length; i++) {
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

        // Retrieve club details from the Club table using the club name
        try (PreparedStatement clubStatement = connections.prepareStatement(clubQuery)) {
            clubStatement.setString(1, clubName);
            ResultSet clubResult = clubStatement.executeQuery();

            if (clubResult.next()) {
                String clubId = clubResult.getString("clubId"); // Get the ID of the club
                String name = clubResult.getString("name");

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

                // Create a list to hold events associated with the club
                ArrayList<Event> clubEvents = new ArrayList<>();

                // Retrieve events associated with the club from the Event table
                try (PreparedStatement eventStatement = connections.prepareStatement(eventQuery)) {
                    eventStatement.setString(1, clubId);
                    ResultSet eventResult = eventStatement.executeQuery();

                    int h = 0;
                    while (eventResult.next()) {
                        String id = eventResult.getString("eventId");
                        String title = eventResult.getString("title");
                        LocalDateTime dateTime = eventResult.getTimestamp("dateTime").toLocalDateTime();
                        String venue =eventResult.getString("venue");
                        String typeOfClubFunction = eventResult.getString("typeOfClubFunction");
                        String description = eventResult.getString("description");

                        Event event = new Event(id,title, dateTime, venue, typeOfClubFunction, description, button[h]);
                        clubEvents.add(event);
                        h++;
                    }
                }

                // Create the Club object with the retrieved data and associated events
                club = new Club(clubId, name, studentsPresent, clubEvents);
            }
        }

        return club;
    }
//=====================================================================

    public void onFillTableClick(ActionEvent event) throws Exception{        //1.  calling the method to fill the table
        allEvents = getClubByName(name).getClubFunctions(); //PUTTING THE STUFF TO THE TABLE
        System.out.println(allEvents.size());    // TO SEE WHETHER ALL THE STUFF ARE LOADED
        loadingEvents();                           // PUTTING IT TO THE TABLE
        fillTable.setDisable(true);
    }

    //=========================================================================
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
        Parent root = loader.load();

        clubAdvisorController cac = loader.getController();
        cac.setWelcomeText(getClubAdvisor(advisorID));

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
                    Club club = new Club(clubId,clubName);        //8. call ClubAdvisor constructor
                    managingClubs.add(club);
                }

                return new ClubAdvisor(id, firstName, lastName, dateOfBirth, password, managingClubs);   //4. call ClubAdvisor constructor
            }
        }

        return null; // ClubAdvisor not found
    }

    //=================================================================
    public void onAssignNewAdvClick(ActionEvent event) throws Exception { //using the advisorID create the object
        //getting the stuff from the text fields
        confirmation = confirmText.getText();    //1. call getText method
        advisorIdOfNewPerson = newAdvisorId.getText();   //2. call getText method //this should be the advisors id

        //get confirmation
        if (confirmation.equals("CONFIRM")) {
            currentClubAdvisor = getClubAdvisor(advisorID);
            newClubAdvisor = getClubAdvisor(advisorIdOfNewPerson);  //3. call getClubAdvisor method
            currentClub=getClubByName(name);                    //5. call getClubAdvisor method

            // Checking if the advisor manages the club
            if (newClubAdvisor ==null) {

                deletingStatus.setText("An advisor with That ID does not Exist");   //9.1 calling setText method
                deletingStatus1.setText("Club not deleted");

            } else {

                boolean status = currentClubAdvisor.assignNewAdvisor(newClubAdvisor,currentClub);   //9.1.1 calling assignNewDriver method
                System.out.println(status);
                 //pass the new advisors id and the club object

                if (status){
                    updateClubAdvisor(currentClub,newClubAdvisor);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
                    Parent root = loader.load();

                    clubAdvisorController cac = loader.getController();
                    cac.setWelcomeText(currentClubAdvisor);

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
            deletingStatus1.setText("Action not confirmed,advisor not assigned.");//11.1 calling setText method
        }
    }
    //=======================================================
    public void onCreateNewEvent(ActionEvent event) throws IOException, SQLException {

        String fileName="/SCMS/FxmlFiles/Event.fxml";      //the fxml path
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();

        EventContorller vsc = loader.getController();
        vsc.gettingInformation(getClubByName(name),advisorID);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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


                student = new Student(id, firstName, lastName, dateOfBirth,password,new CheckBox());
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return student;
    }
    //=======================================================




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
    //=====================================================================
    public void onGenerateReportButtonClicked(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Generate Reports.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Club club = getClubByName(name);
        GenerateReportsController GRC = loader.getController();

        GRC.Start(club);
        GRC.gettingInformation(getClubByName(name),advisorID);

        stage.setScene(scene);
        stage.show();
    }
}



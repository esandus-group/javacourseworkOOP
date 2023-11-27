package SCMS.Controllers;

import SCMS.HelloApplication;
import SCMS.Objects.Club;
import SCMS.Objects.Event;
import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

// Controller for managing events
public class EventContorller implements Initializable {
    // FXML elements
    @FXML
    private TextField eventTitle;
    @FXML
    private DatePicker eventDate;
    @FXML
    private TextField eventVenue;
    @FXML
    private TextField eventDescription;
    @FXML
    private ChoiceBox<String> eventTypeChoiceBox;
    @FXML
    private Button backButtonCDD;
    @FXML
    private TextField timeHourTextBoxData;
    @FXML
    private TextField timeMinuteTextBoxData;
    @FXML
    private Label errorMessageLabel;

    // Date and time variables
    private LocalDateTime dateTime;
    private String errorMessage = "";

    // Other variables
    private String[] typeList = {"Event", "Meeting", "Activity"};
    private String type = "";
    private static Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
    private String buttonText;
    private Club club;
    private String adId;

    // Method to pass information to the controller
    public void gettingInformation(Club club, String advisorId) {       //1. getting the information from previ controller
        this.club = club;
        this.adId = advisorId;
    }

    // Back button action
    public void backButtonCDD(ActionEvent event) throws Exception {
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        buttonText = club.getName();
        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText, adId);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // SQL reader method to get number of events, meetings and activities per month
    public static HashMap<String, String> SQLReader(String yearMonth) {
        try {
            String sqlQuery = "SELECT typeOfClubFunction, COUNT(*) AS numberOfEvents FROM Event WHERE DATE_FORMAT(dateTime, '%Y-%m') = ? GROUP BY typeOfClubFunction";
            PreparedStatement statement = connections.prepareStatement(sqlQuery);
            statement.setString(1, yearMonth);
            ResultSet resultSet = statement.executeQuery();

            // Putting the results in a hashmap for easy access
            HashMap<String, String> eventCount = new HashMap<String, String>();
            while (resultSet.next()) {
                System.out.println("here");
                eventCount.put(resultSet.getString("typeOfClubFunction"), resultSet.getString("numberOfEvents"));
            }
            System.out.println(eventCount.get("Meeting"));
            return eventCount;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Validate time and type of event
    public String validateTimeAndTypeOfEvent(LocalDate date, String hour, String minute, String typ) {
        Integer eventHour;
        Integer eventMinute;
        // Validating if the input are integers
        try {
            eventHour = Integer.parseInt(hour);
            eventMinute = Integer.parseInt(minute);
        } catch (NumberFormatException e) {
            errorMessage = "Time should be a number";
            errorMessageLabel.setText(errorMessage);
            return errorMessage;
        }
        // If integers, validating if its a valid time
        // Any function can be only held from 4am to 6pm
        if ((eventHour > 18 || eventHour <= 4) || (eventMinute > 59 || eventHour <= 0)) {
            errorMessage = "Any club activity should be held between 4.00 to 18.00";
            errorMessageLabel.setText(errorMessage);
            return errorMessage;
        }
        if (eventMinute > 59 || eventHour <= 0) {
            errorMessage = "Minutes should be within 0-59";
            errorMessageLabel.setText(errorMessage);
            return errorMessage;
        }
        // Setting time in the DateTime format
        LocalTime time = LocalTime.of(eventHour, eventMinute);
        dateTime = date.atTime(time);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDateTime = dateTime.format(formatter);
        // Any event can have only 2 events, 1 meetings and 2 activities per month
        HashMap<String, String> eventCount = SQLReader(formattedDateTime);
        int noOfEvents = 0;
        int noOfMeetings = 0;
        int noOfActivities = 0;
        if (eventCount.containsKey("Event")) {
            noOfEvents = Integer.parseInt(eventCount.get("Event"));
        }
        if (eventCount.containsKey("Meeting")) {
            noOfMeetings = Integer.parseInt(eventCount.get("Meeting"));
        }
        if (eventCount.containsKey("Activity")) {
            noOfActivities = Integer.parseInt(eventCount.get("Activity"));
        }

        if (type.equalsIgnoreCase("Event") && noOfEvents == 2) {
            errorMessage = "Only 2 events per month can be created.";
            return errorMessage;
        }
        if (type.equalsIgnoreCase("Meeting") && noOfMeetings == 1) {
            errorMessage = "Only 1 meeting per month can be created.";
            return errorMessage;
        }
        if (type.equalsIgnoreCase("Activity") && noOfActivities == 4) {
            errorMessage = "Only 2 activity per month can be created.";
            return errorMessage;
        }
        return errorMessage;
    }

    // Validate text fields for empty values
    public String validateTextFileds(String title, String venue, String description, String type) {
        if (title.isEmpty() || venue.isEmpty() || description.isEmpty() || type.isEmpty()) {
            errorMessage = "Fields cannot be empty";
        }
        return errorMessage;
    }

    // Initialization method for TypeChoiceBox
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventTypeChoiceBox.getItems().addAll(typeList);
        eventTypeChoiceBox.setOnAction(this::onCreateNewEvent);
    }

    // Getting all data and creating the new event object if inputs are valid
    public void onCreateNewEvent(ActionEvent event) {        //2.1 calling the method to create the event
        errorMessage = "";
        String title = eventTitle.getText();
        String venue = eventVenue.getText();
        String description = eventDescription.getText();
        type = eventTypeChoiceBox.getValue();

        errorMessage = (validateTextFileds(title, venue, description, type));       //2.1. calling the method to validate inputs
        errorMessageLabel.setText(errorMessage);                                   //4.2. calling the method to setText
        System.out.println(errorMessage);

        if (errorMessage.equalsIgnoreCase("")) {
            LocalDate date = eventDate.getValue();
            String strHour = timeHourTextBoxData.getText();
            String strMinute = timeMinuteTextBoxData.getText();
            errorMessage = validateTimeAndTypeOfEvent(date, strHour, strMinute, type);       //2.2. method to validate the time and no.of events
            errorMessageLabel.setText(errorMessage);                                     //4.2. calling the method to setText
            System.out.println(errorMessage);

            if (errorMessage.equalsIgnoreCase("")) {
                eventTitle.setText("");
                eventVenue.setText("");
                eventDescription.setText("");
                timeHourTextBoxData.setText("");
                timeMinuteTextBoxData.setText("");
                eventDate.setValue(null);
                eventTypeChoiceBox.setValue(null);
                errorMessageLabel.setText("");

                Event newEvent = new Event(title, dateTime, venue, type, description, new Button());   //3. Calling event Constructor

                newEvent.writeEventsToSqlDB(club.getClubId());              //4.1. calling the method to add to the database
            }
        }
    }
}

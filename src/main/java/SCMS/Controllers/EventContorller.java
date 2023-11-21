package SCMS.Controllers;

import SCMS.HelloApplication;
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

import java.io.IOException;
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

public class EventContorller implements Initializable {
    HelloApplication  helloApplicationInstance = new HelloApplication();
    @FXML
    TextField eventTitle;
    @FXML
    DatePicker eventDate;
    @FXML
    TextField eventVenue;
    @FXML
    TextField eventDescription;
    @FXML
    ChoiceBox<String> eventTypeChoiceBox;
    String[] typeList = {"Event", "Meeting", "Activity"};
    String type = "";
    private static Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();

    @FXML
    private Button backButtonCDD;
    @FXML
    TextField clubIdTextBoxData;
    @FXML
    TextField timeHourTextBoxData;
    @FXML
    TextField timeMinuteTextBoxData;
    LocalDateTime dateTime;
    String errorMessage = "";
    @FXML
    Label errorMessageLabel;
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        helloApplicationInstance.stageLoader(event,fileName);
    }

    public static HashMap<String, String> SQLReader(String yearMonth){
//        yearMonth format => "2023-11"
        try {
            String sqlQuery = "SELECT typeOfClubFunction, COUNT(*) AS numberOfEvents FROM Event WHERE DATE_FORMAT(dateTime, '%Y-%m') = ? GROUP BY typeOfClubFunction";

            // Create a PreparedStatement
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
    public String validateTime(LocalDate date, String hour, String minute, String typ) {

        Integer eventHour;
        Integer eventMinute;
        try {
             eventHour = Integer.parseInt(hour);
             eventMinute = Integer.parseInt(minute);


        }
        catch (NumberFormatException e) {
            errorMessage = "Time should be a number";
            errorMessageLabel.setText(errorMessage);
            return errorMessage;
        }

        if ((eventHour > 18 || eventHour<=4)||(eventMinute > 59 || eventHour<=0)){
                errorMessage = "Any club activity should be held between 4.00 to 18.00";
                errorMessageLabel.setText(errorMessage);
                return errorMessage;
            }
        if (eventMinute > 59 || eventHour<=0){
            errorMessage = "Minutes should be within 0-59";
            errorMessageLabel.setText(errorMessage);
            return errorMessage;
        }
        LocalTime time = LocalTime.of(eventHour, eventMinute);
        dateTime = date.atTime(time);



        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDateTime = dateTime.format(formatter);

        HashMap<String, String> eventCount = SQLReader(formattedDateTime);
        int noOfEvents = 0;
        int noOfMeetings = 0;
        int noOfActivities = 0;
        if(eventCount.containsKey("Event")){
            noOfEvents = Integer.parseInt(eventCount.get("Event"));
        }
        if(eventCount.containsKey("Meeting")){
            noOfMeetings = Integer.parseInt(eventCount.get("Meeting"));
        }
        if(eventCount.containsKey("Activity")){
            noOfActivities = Integer.parseInt(eventCount.get("Activity"));
        }

        if(type.equalsIgnoreCase("Event") && noOfEvents==2){
            errorMessage = "Only 2 events per month can be created.";
            return errorMessage;
        }
        if(type.equalsIgnoreCase("Meeting") && noOfMeetings==1){
            errorMessage = "Only 1 meeting per month can be created.";
            return errorMessage;
        }
        if(type.equalsIgnoreCase("Activity") && noOfActivities==4){
            errorMessage = "Only 2 activity per month can be created.";
            return errorMessage;
        }
        return errorMessage;
    }


    public String validateTextFiledTimePickerChoiceBox(String title, String venue, String description, String clubId, String type){
        if(title.isEmpty() || venue.isEmpty()  || description.isEmpty() || clubId.isEmpty() || type.isEmpty()){
            errorMessage = "Fields cannot be empty";


        }
        return errorMessage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventTypeChoiceBox.getItems().addAll(typeList);
        eventTypeChoiceBox.setOnAction(this::onCreateNewEvent);

    }

    public void onCreateNewEvent(ActionEvent event) {
        errorMessage = "";
        String title = eventTitle.getText();
        String venue = eventVenue.getText();
        String description = eventDescription.getText();
        String clubId = clubIdTextBoxData.getText();
        type = eventTypeChoiceBox.getValue();

        System.out.println(title+ venue+description+ clubId+type);
        errorMessage = (validateTextFiledTimePickerChoiceBox(title, venue, description, clubId,type));
        errorMessageLabel.setText(errorMessage);
        System.out.println(errorMessage);

            if(errorMessage.equalsIgnoreCase("")){
                LocalDate date = eventDate.getValue();
                String strHour = timeHourTextBoxData.getText();
                String strMinute = timeMinuteTextBoxData.getText();
                errorMessage = validateTime(date,strHour,strMinute,type);
                errorMessageLabel.setText(errorMessage);
                System.out.println(errorMessage);


                if (errorMessage.equalsIgnoreCase("")){
                    eventTitle.setText("");
                    eventVenue.setText("");
                    eventDescription.setText("");
                    clubIdTextBoxData.setText("");
                    timeHourTextBoxData.setText("");
                    timeMinuteTextBoxData.setText("");
                    eventDate.setValue(null);
                    eventTypeChoiceBox.setValue(null);
                    errorMessageLabel.setText("");


                    Event newEvent = new Event(title, dateTime,venue, type, description, clubId);
                    newEvent.writeEventsToSqlDB();
                }

        }

    }}



















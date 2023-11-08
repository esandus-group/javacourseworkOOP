package SCMS.Controllers;

import SCMS.Objects.Event;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
//frnn


public class EventContorller implements Initializable {
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


    public void stageLoader(ActionEvent event, String fileName) throws IOException { //STAGE LOADER METHOD
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }



    public String validateTime(LocalDate date, String hour, String minute) {

        Integer eventHour;
        Integer eventMinute;
        try {
             eventHour = Integer.parseInt(hour);
             eventMinute = Integer.parseInt(minute);


        }
        catch (NumberFormatException e) {
            errorMessage = "Invalid String";
            errorMessageLabel.setText(errorMessage);
            return errorMessage;
        }

        if ((eventHour > 18 || eventHour<=0)||(eventMinute > 59 || eventHour<=0)){
                errorMessage = "Hour and Minutes should be within range";
                errorMessageLabel.setText(errorMessage);
                return errorMessage;
            }
        LocalTime time = LocalTime.of(eventHour, eventMinute);
        dateTime = date.atTime(time);

        return errorMessage;
    }


    public String validateTextFiledTimePickerChoiceBox(String title, String venue, String description, String clubId, String type){
        if(title.isEmpty() || venue.isEmpty()  || description.isEmpty() || clubId.isEmpty() || type.isEmpty()){
            errorMessage = "Fields cannot be empty";
            errorMessageLabel.setText(errorMessage);

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
        System.out.println(errorMessage);

            if(errorMessage.equalsIgnoreCase("")){
                LocalDate date = eventDate.getValue();
                String strHour = timeHourTextBoxData.getText();
                String strMinute = timeMinuteTextBoxData.getText();
                errorMessage = validateTime(date,strHour,strMinute);
                System.out.println(errorMessage);
                if (errorMessage.equalsIgnoreCase("")){

                    Event newEvent = new Event(title, dateTime,venue, type, description, clubId);
                    newEvent.writeEventsToSqlDB();
                }

        }

    }}



















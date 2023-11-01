package SCMS.Controllers;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.w3c.dom.ls.LSOutput;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
    String type;
    String clubId = "C1";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventTypeChoiceBox.getItems().addAll(typeList);
        eventTypeChoiceBox.setOnAction(this::getType);

    }

    public void getType(ActionEvent event){
        type = eventTypeChoiceBox.getValue();
    }

    public void onCreateNewEvent(ActionEvent event) {
        String title = eventTitle.getText();
        LocalDate date = eventDate.getValue();
        String venue = eventVenue.getText();
        String description = eventDescription.getText();
        System.out.println(type + date.toString());
//        Event newEvent = new Event(title,date, venue, type, description, clubId);


    }







}

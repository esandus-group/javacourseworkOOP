package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;


public class GenerateReportsController {
    public ArrayList<Event> Functions = new ArrayList<>();

    @FXML
    private TableColumn<Event, String> attendanceCol;

    @FXML
    private TableColumn<Event, String> dateCol;

    @FXML
    private TableColumn<Event, String> eventNameCol;

    @FXML
    private TableColumn<Event, String> eventTypeCol;

    @FXML
    private TableColumn<Event, String> eventVenueCol;

    @FXML
    private TableView<Event> reportTable;

    Club club;

    @FXML
    private DatePicker datePickerFilter;
    public void Start(Club selectedClub){
        this.club = selectedClub;
        Functions = club.getClubFunctions();
    }
    public void loadingEvents() {
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventVenueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));

        attendanceCol.setCellValueFactory(cellData -> {
            int attendance = cellData.getValue().getStudentsWhoJoined().size();
            return new SimpleStringProperty(String.valueOf(attendance));
        });

        ObservableList<Event> data = FXCollections.observableArrayList();

        if (datePickerFilter.getValue() != null) {
            data.addAll(Functions.stream()
                    .filter(event -> event.getDateTime().toLocalDate().isEqual(datePickerFilter.getValue()))
                    .toList());
        } else {
            data.addAll(Functions);
        }

        reportTable.setItems(data);
    }







}

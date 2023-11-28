package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.Event;
import SCMS.Utils.SCMSEnvironment;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class GenerateReportsController {
    public ArrayList<Event> Functions = new ArrayList<>();
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB
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
    public String buttonText;
    @FXML
    private DatePicker datePickerFilter;
    public void Start(Club selectedClub){
        this.club = selectedClub;
        Functions = club.getClubFunctions();
    }
    public String adId;
    public Club clubObject;
    public void gettingInformation(Club club, String advisorId) {
        this.clubObject = club;
        this.adId = advisorId;
    }

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

    public void loadingEvents() {
        eventTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
        eventNameCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        eventVenueCol.setCellValueFactory(new PropertyValueFactory<>("venue"));

        attendanceCol.setCellValueFactory(cellData -> {
            int attendance = 0;
            try {
                attendance = getEventAttendanceCount(cellData.getValue().getEventId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

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

    public int getEventAttendanceCount(String eventId) throws SQLException {
        String attendanceQuery = "SELECT COUNT(*) FROM Attendance WHERE eventId = ?";

        try (PreparedStatement attendanceStatement = connections.prepareStatement(attendanceQuery)) {
            attendanceStatement.setString(1, eventId);
            ResultSet attendanceResult = attendanceStatement.executeQuery();

            if (attendanceResult.next()) {
                return attendanceResult.getInt(1); // gets the count from the first column
            }
        }

        return 0; // Return 0 if no attendance count is found means no student has joined
    }
}

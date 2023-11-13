package SCMS.Controllers;

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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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


    @FXML
    private Text clubNameText;
    String clubName;
    String stdName;
    String stdId;

    public void setClubNameText(String clubName, String studentName) {
        this.clubName = clubName;
        this.stdName = studentName;
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

            Statement st = connections.createStatement();

            String stdIdQuery = "SELECT * FROM student WHERE firstName = '" + stdName + "'";
            ResultSet stdIdResult = st.executeQuery(stdIdQuery);
            if(stdIdResult.next()){
                stdId = stdIdResult.getString(1);
            }

            String clubIdQuery = "SELECT clubId FROM club WHERE name = '" + clubName + "'";
            ResultSet clubIdResult = st.executeQuery(clubIdQuery);

            if (clubIdResult.next()) {
                String clubId = clubIdResult.getString("clubId");

                System.out.println(clubId+"Leave club");
                System.out.println(stdId);
                String deleteQuery = "DELETE FROM club_student WHERE clubId = '" + clubId + "' AND id = '" + stdId + "'";
                int deletedRows = st.executeUpdate(deleteQuery);

                if (deletedRows > 0) {
                    System.out.println("Student removed from the club.");
                }
                } else {
                    // Club not found, handle this situation accordingly
                    System.out.println("Club not found.");
                }
            clubIdResult.close();
            st.close();
            connections.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
        Parent root = loader.load();
        StudentDashboardController SDC = loader.getController();
        SDC.setWelcomeText(stdName);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}

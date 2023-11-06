package SCMS.Controllers;

import SCMS.HelloApplication;
import SCMS.Objects.Club;
import SCMS.Objects.ClubAdvisor;
import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class PressClubController {
    HelloApplication helloApplicationInstance = new HelloApplication();
    @FXML
    private TextField clubIdToDelete;

    public String confirmation;
    public String clubIdDelete;


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
    private TextField advisorIdDeleting;

    String advisorIdWhoIsDeleting;

    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private TableColumn<?, ?> colAttendance;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colFunction;

    @FXML
    private Button removeStudent;


    @FXML
    private TableView<?> toUsers;
    @FXML
    private Button createNewEvent;

    Stage stage;
    ClubAdvisor currentClubAdvisor = null;

    Club currentClub = null;

    public void stageLoader(ActionEvent event, String fileName) throws IOException { //STAGE LOADER METHOD
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //=======================================================
    public void onRemoveStudentClick(ActionEvent event) throws Exception{
        String fileName="/SCMS/FxmlFiles/DeleteStudent.fxml";      //open the page
        stageLoader(event,fileName);

    }
    //=======================================================
    public void onViewStudentsButtonClick(ActionEvent event) throws Exception{
        String fileName="/SCMS/FxmlFiles/view Students.fxml";      //open the page
        stageLoader(event,fileName);

    }
    //=======================================================
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
    public void deleteClub(String clubId) { //modify to pass the object and use getters to get infor

        try {
            // Delete all students from the Club_Student table
            String deleteStudentsQuery = "DELETE FROM Club_Student WHERE clubId = ?";
            try (PreparedStatement studentsStatement = connections.prepareStatement(deleteStudentsQuery)) {
                studentsStatement.setString(1, clubId);
                studentsStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("All students associated with Club ID " + clubId + " have been removed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //------------------------------------------------------------------------------
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

    //=======================================================
    public void onDeleteClubClick(ActionEvent event) throws Exception { //using the advisorID create the object
        confirmation = confirmText.getText();                          //getting the stuff from the text fields
        clubIdDelete = clubIdToDelete.getText();
        advisorIdWhoIsDeleting =advisorIdDeleting.getText();

        if (confirmation.equals("CONFIRM")) {

            // Checking if the advisor manages the club
            if (!checkIfAdvisorManagesClub(clubIdDelete, advisorIdWhoIsDeleting)) {

                currentClubAdvisor = getClubAdvisor(advisorIdWhoIsDeleting);
                currentClubAdvisor.deleteClub(clubIdDelete);
                    deletingStatus.setText("An advisor with That ID does not manage a Club");
                    deletingStatus1.setText("Club not deleted");


                    // Proceed with club deletion
                    deleteClub(clubIdDelete);          //have to change and then the advisor cant make a new one

                    //clearing the text fields
                    confirmText.clear();
                    clubIdToDelete.clear();
                    advisorIdDeleting.clear();

            } else {

                deletingStatus.setText("An advisor with That ID does not manage a Club");
                deletingStatus1.setText("Club not deleted");

            }
        } else {
            deletingStatus1.setText("Deletion not confirmed. Club was not deleted.");
        }
    }

    public void onCreateNewEvent(ActionEvent event) throws IOException {
        helloApplicationInstance.stageLoader(event, "/SCMS/FxmlFiles/Event.fxml");
    }
    //=========================================================================
}

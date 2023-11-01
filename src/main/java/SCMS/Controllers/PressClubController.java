package SCMS.Controllers;

import SCMS.HelloApplication;
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

    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();

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

    public void stageLoader(ActionEvent event, String fileName) throws IOException {
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
    public void deleteClub(String clubId) {
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
    public boolean checkIfClubExists(String clubId) {
        String query = "SELECT 1 FROM Club WHERE clubId = ?";
        boolean clubExists = false;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, clubId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                clubExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return clubExists;
    }

    //=======================================================
    public void onDeleteClubClick(ActionEvent event) throws Exception {
        confirmation = confirmText.getText();
        clubIdDelete = clubIdToDelete.getText();
        advisorIdWhoIsDeleting =advisorIdDeleting.getText();

        if (confirmation.equals("CONFIRM")) {
            // Check if the advisor manages the club
            if (checkIfAdvisorManagesClub(clubIdDelete, advisorIdWhoIsDeleting)) {
                // Proceed with club deletion
                deleteClub(clubIdDelete);
                System.out.println("Club with ID " + clubIdDelete + " has been deleted.");
                confirmText.clear();
                clubIdToDelete.clear();
                advisorIdDeleting.clear();
            } else {
                deletingStatus.setText("An advisor with That ID does not manage a Club");
                deletingStatus1.setText("Club not deleted");

                System.out.println("Advisor with ID " + advisorIdWhoIsDeleting + " does not manage the club with ID " + clubIdDelete + ". Club was not deleted.");
            }
        } else {
            deletingStatus1.setText("Deletion not confirmed. Club was not deleted.");
            System.out.println("Deletion not confirmed. Club was not deleted.");
        }
    }

    public void onCreateNewEvent(ActionEvent event) throws IOException {
        helloApplicationInstance.stageLoader(event, "/SCMS/FxmlFiles/Event.fxml");
    }
    //=========================================================================
}

package SCMS.Controllers;

import SCMS.Utils.SCMSEnvironment;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class RemoveStudentController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
    @FXML
    private TextField deleteStudentClub;

    @FXML
    private TextField deleteStudentId;

    @FXML
    private TextField deleteStudentReason;
    @FXML
    private Label reasonStatus;
    @FXML
    private Label clubIdStatus;
    @FXML
    private Label studentIdStatus;
    @FXML
    private Button removeStudentButton;

    @FXML
    private Label statusShowLabel;

    //=======================================================
    public String getStudentFirstName(String studentId) {
        String firstName = null;
        String query = "SELECT firstName FROM Student WHERE id = ?";

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, studentId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                firstName = resultSet.getString("FirstName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception here
        }

        return firstName;
    }
    //=======================================================
    public void saveRemovedStudent(String clubId, String studentId, String studentFirstName, String reason) {
        try {

            String query = "INSERT INTO RemovedStudents (clubid, studentid, studentFirstName, reason) VALUES (?, ?, ?, ?)";

            try (PreparedStatement statement = connections.prepareStatement(query)) {
                statement.setString(1, clubId);
                statement.setString(2, studentId);
                statement.setString(3, studentFirstName);
                statement.setString(4, reason);

                int rowsInserted = statement.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("Removed student added to the database.");
                } else {
                    System.out.println("Failed to add the removed student to the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //=======================================================
    public void removeStudentFromClub(String clubId, String studentId) {
        try {

            String query = "DELETE FROM Club_Student WHERE clubid = ? AND id = ?";

            try (PreparedStatement statement = connections.prepareStatement(query)) {
                statement.setString(1, clubId);
                statement.setString(2, studentId);

                int rowsDeleted = statement.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Student removed from club.");
                } else {
                    System.out.println("Failed to remove the student from the club.");
                }

            } catch (SQLException e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //=======================================================
    public boolean studentExistsInClub(String clubId, String studentId) {
        String query = "SELECT 1 FROM Club_Student WHERE clubid = ? AND id = ?";

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, clubId);
            statement.setString(2, studentId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If the result set has a row, the student exists in the club.
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception here.
            return false; // Return false in case of an exception.
        }
    }
    public boolean isStudentIdValid(String studentId){
        if ( studentId== null || studentId.equals("")){
            return false;
        }
        return true;
    }
    public boolean isClubIdValid(String clubId){
        if ( clubId== null || clubId.equals("")){
            return false;
        }
        return true;
    }
    public boolean isReasonValid(String reason){
        if ( reason== null || reason.equals("")){
            return false;
        }
        return true;
    }
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
    public void onRemoveStudentButtonClick(ActionEvent event) throws Exception {
        String studentId = deleteStudentId.getText();
        String clubIdToDeleteStudent = deleteStudentClub.getText(); // Assuming clubId is a string
        String reason = deleteStudentReason.getText();
        String firstNameOfStudent= getStudentFirstName(studentId);

        if (!isStudentIdValid(studentId)) {
            studentIdStatus.setText("please enter the ID");
            return;
        }
        if (!isClubIdValid(clubIdToDeleteStudent)) {
            clubIdStatus.setText("please enter the ID");
            return;
        }
        if (!isReasonValid(reason)) {
            reasonStatus.setText("please enter the Reason");
            return;
        }
        if (!checkIfClubExists(clubIdToDeleteStudent)){
            statusShowLabel.setText("Club not found");
            return;
        }

        // Checking if the student ID exists in the club_student table
        if (!studentExistsInClub(clubIdToDeleteStudent, studentId)) {
            System.out.println("Student with ID " + studentId + " does not exist in the club.");
            statusShowLabel.setText("Student not Found");
            return;
        }

        // Calling the methods to save the removed student and remove them from the club
        saveRemovedStudent(clubIdToDeleteStudent, studentId,firstNameOfStudent, reason);
        removeStudentFromClub(clubIdToDeleteStudent, studentId);

        deleteStudentId.clear();
        deleteStudentClub.clear();
        deleteStudentReason.clear();
    }
}

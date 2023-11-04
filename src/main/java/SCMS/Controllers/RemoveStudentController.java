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
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();//getting the connection
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
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


    //checking code part
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
        //getting the information
        String studentId = deleteStudentId.getText();
        String clubIdToDeleteStudent = deleteStudentClub.getText(); // Assuming clubId is a string
        String reason = deleteStudentReason.getText();

        //checking whether they are empty
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

        //checking if the club exists to delete the student
        if (!checkIfClubExists(clubIdToDeleteStudent)) {
            statusShowLabel.setText("Club not found");
            return;
        }

        //checking if that student is in the club to delete
        // Checking if the student ID exists in the club_student table
        if (!studentExistsInClub(clubIdToDeleteStudent, studentId)) {
            System.out.println("Student with ID " + studentId + " does not exist in the club.");
            statusShowLabel.setText("Student not Found");
            return;
        }

        String firstNameOfStudent= getStudentFirstName(studentId); //getting the first name of the student


        // Calling the methods to save the removed student table and remove them from the club_student table
        saveRemovedStudent(clubIdToDeleteStudent, studentId,firstNameOfStudent, reason);

        removeStudentFromClub(clubIdToDeleteStudent, studentId);

        deleteStudentId.clear();
        deleteStudentClub.clear();
        deleteStudentReason.clear();

    }
}

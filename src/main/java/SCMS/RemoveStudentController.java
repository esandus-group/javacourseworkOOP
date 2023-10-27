package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class RemoveStudentController {

    @FXML
    private TextField deleteStudentClub;

    @FXML
    private TextField deleteStudentId;

    @FXML
    private TextField deleteStudentReason;

    @FXML
    private Button removeStudentButton;
    private Connection connections;
    @FXML
    private Label statusShowLabel;
    //=======================================================
    public RemoveStudentController() {
        try {
            // Initialize the database connection when the controller is created
            String url = "jdbc:mysql://localhost:3306/school_club_management";
            String username = "root";
            String password = "esandu12345";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connections = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
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
    //=======================================================
    public void onRemoveStudentButtonClick(ActionEvent event) throws Exception {
        String studentId = deleteStudentId.getText();
        String clubIdToDeleteStudent = deleteStudentClub.getText(); // Assuming clubId is a string
        String reason = deleteStudentReason.getText();
        String firstNameOfStudent= getStudentFirstName(studentId);

        if (studentId.isEmpty() || clubIdToDeleteStudent.isEmpty() || reason.isEmpty()) {
            // Handle empty input fields (you can show an error message)
            System.out.println("Please fill in all fields.");
            return;
        }
        // Check if the student ID exists in the club_student table
        if (!studentExistsInClub(clubIdToDeleteStudent, studentId)) {
            System.out.println("Student with ID " + studentId + " does not exist in the club.");
            // You can show an error message here or handle it as needed.
            return;
        }

        // Call the methods to save the removed student and remove them from the club
        saveRemovedStudent(clubIdToDeleteStudent, studentId,firstNameOfStudent, reason);
        removeStudentFromClub(clubIdToDeleteStudent, studentId);

        // Optionally, you can clear the input fields or perform other actions
        deleteStudentId.clear();
        deleteStudentClub.clear();
        deleteStudentReason.clear();
    }


}

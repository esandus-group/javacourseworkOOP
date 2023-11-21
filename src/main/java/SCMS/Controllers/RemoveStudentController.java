package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.ClubAdvisor;
import SCMS.Objects.Student;
import SCMS.Utils.SCMSEnvironment;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class RemoveStudentController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //getting the connection
    @FXML
    private TextField deleteStudentClub;
    @FXML
    private Button backButtonCDD;
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

    ClubAdvisor currentClubAdvisor = null;

    Club currentClub = null;

    @FXML
    private Label statusShowLabel;
    private Club club;
    private String adId;

    public void gettingInformation(Club club, String adId){
        this.club=club;
        this.adId=adId;
    }

    //=====================================

    public void  backButtonCDD  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        String buttonText = club.getName();

        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText,adId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //====================================
    public Student getStudent(String studentId) {
        String query = "SELECT * FROM Student WHERE id = ?";
        Student student = null;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, studentId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                // Retrieving student details from the result set
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String dateOfBirth = resultSet.getString("dateOfBirth");
                String password =  resultSet.getString("password");


                // Creating the student onject and returning it
                student = new Student(id, firstName, lastName, dateOfBirth,password);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return student;
    }
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
                    Club club = new Club(clubId,clubName);
                    managingClubs.add(club);
                }

                return new ClubAdvisor(id, firstName, lastName, dateOfBirth, password, managingClubs);
            }
        }

        return null; // ClubAdvisor not found
    }


    //=======================================================//saving to the removed students table
    public void saveRemovedStudent(String clubId, String studentId, String studentFirstName, String reason) {
        try {

            String query = "INSERT INTO RemovedStudents (clubId, studentId, studentFirstName, reason) VALUES (?, ?, ?, ?)";

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
    public void removeStudentFromClub(Club club, String studentId) {
        String clubId = club.getClubId();
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
    //=====================================================
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

        String firstNameOfStudent= getStudent(studentId).getFirstName(); //getting the first name of the student

        currentClubAdvisor = getClubAdvisor(adId);

        currentClubAdvisor.removeStudent(studentId,club);
        // Calling the methods to save the removed student table and remove them from the club_student table
        saveRemovedStudent(clubIdToDeleteStudent, studentId,firstNameOfStudent, reason);

        removeStudentFromClub(club, studentId);

        deleteStudentId.clear();
        deleteStudentClub.clear();
        deleteStudentReason.clear();
        statusShowLabel.setText("");
        clubIdStatus.setText("");
        studentIdStatus.setText("");


    }
}

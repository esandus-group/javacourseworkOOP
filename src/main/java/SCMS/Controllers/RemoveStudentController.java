package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.ClubAdvisor;
import SCMS.Objects.Student;
import SCMS.Utils.SCMSEnvironment;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;
import java.util.ArrayList;

public class RemoveStudentController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //getting the connection
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

    ClubAdvisor currentClubAdvisor = null;

    Club currentClub = null;

    @FXML
    private Label statusShowLabel;
    public Student getStudent(String studentId) {
        String query = "SELECT * FROM Student WHERE id = ?";
        Student student = null;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, studentId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Retrieve student details from the result set
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String dateOfBirth = resultSet.getString("dateOfBirth");
                String password =  resultSet.getString("password");
                // Add more fields as needed

                // Create a Student object with the retrieved data
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
                    Club club = new Club(clubId,clubName,id);
                    managingClubs.add(club);
                }

                return new ClubAdvisor(id, firstName, lastName, dateOfBirth, password, managingClubs);
            }
        }

        return null; // ClubAdvisor not found
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
        }

        return firstName;
    }
    //=======================================================
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
    public Club getClub(String clubId) throws SQLException {
        String clubQuery = "SELECT * FROM Club WHERE clubId = ?";
        String clubStudentQuery = "SELECT * FROM Club_Student WHERE clubId = ?";

        Club club = null;

            // Retrieve club details from the Club table
            try (PreparedStatement clubStatement = connections.prepareStatement(clubQuery)) {
                clubStatement.setString(1, clubId);
                ResultSet clubResult = clubStatement.executeQuery();

                if (clubResult.next()) {
                    String name = clubResult.getString("name");
                    String idOfAdvisor = clubResult.getString("idOfAdvisor");

                    // Create a list to hold students present
                    ArrayList<Student> studentsPresent = new ArrayList<>();

                    // Retrieve student IDs associated with the club from the Club_Student table
                    try (PreparedStatement clubStudentStatement = connections.prepareStatement(clubStudentQuery)) {
                        clubStudentStatement.setString(1, clubId);
                        ResultSet clubStudentResult = clubStudentStatement.executeQuery();

                        while (clubStudentResult.next()) {
                            String studentId = clubStudentResult.getString("id");
                            // Fetch student objects using the studentId and add to the list
                            Student student = getStudent(studentId);
                            if (student != null) {
                                studentsPresent.add(student);
                            }
                        }
                    }

                    // Create the Club object with the retrieved data
                    club = new Club(clubId, name, idOfAdvisor, studentsPresent);
                }
            }


        return club;
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

        String firstNameOfStudent= getStudent(studentId).getFirstName(); //getting the first name of the student
        currentClub = getClub(clubIdToDeleteStudent);
        currentClubAdvisor = getClubAdvisor(currentClub.getIdOfAdvisor());

        currentClubAdvisor.removeStudent(studentId,currentClub);
        // Calling the methods to save the removed student table and remove them from the club_student table
        saveRemovedStudent(clubIdToDeleteStudent, studentId,firstNameOfStudent, reason);

        removeStudentFromClub(clubIdToDeleteStudent, studentId);

        deleteStudentId.clear();
        deleteStudentClub.clear();
        deleteStudentReason.clear();
        statusShowLabel.setText("");
        clubIdStatus.setText("");
        studentIdStatus.setText("");


    }
}

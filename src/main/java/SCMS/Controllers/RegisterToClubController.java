package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.Student;
import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

//Join Club is fully functional
public class RegisterToClubController {
//Load data to the 2 array lists.
    @FXML
    private Text IDerrorLabel;
    ArrayList<String> clubs = new ArrayList<>();
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB
    @FXML
    private TextField stdIdTextField;
    @FXML
    private TextField stdNameTextField;
    @FXML
    private ComboBox clubComboBox;

    String studentFName;
    String stdId;
    private Student student;
    public void InitializeStudent(Student std) throws Exception {
        this.student = std;
        this.studentFName = student.getFirstName();
        this.stdId = student.getId();
        setClubsComboBox();

    }

    public String getClubIdByName(String name) throws SQLException {
        Statement st = connections.createStatement();
        String getClubId = "SELECT * FROM club WHERE name='"+name+"'";
        ResultSet selectedClubIdrs = st.executeQuery(getClubId);
        if(selectedClubIdrs.next()){
            return selectedClubIdrs.getString("clubId");
        }
        return null;
    }

    public void onRegisterToClubButtonClick(ActionEvent event) throws Exception{
        String studentId = stdIdTextField.getText();
        String studentName = stdNameTextField.getText();
        String selectedClub = clubComboBox.getValue().toString();


        if (isStudentValid(studentId, studentName)) {
            if (!isStudentRemoved(studentId, getClubIdByName(selectedClub))){
                student.joinClub(getClubByName(selectedClub));
                registerStudentToClub(event, studentId, getClubIdByName(selectedClub));
            }
        }
    }
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
    public Club getClubByName(String clubName) throws SQLException {
        String clubQuery = "SELECT * FROM Club WHERE name = ?";
        String clubStudentQuery = "SELECT * FROM Club_Student WHERE clubId = ?";

        Club club = null;

        // Retrieve club details from the Club table using the club name
        try (PreparedStatement clubStatement = connections.prepareStatement(clubQuery)) {
            clubStatement.setString(1, clubName);
            ResultSet clubResult = clubStatement.executeQuery();

            if (clubResult.next()) {
                String clubId = clubResult.getString("clubId"); // Get the ID of the club
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
    public void setClubsComboBox() {
        try {
            Statement st = connections.createStatement();
            // Get the clubs that the student has not joined
            String clubsNotJoinedQuery = "SELECT * FROM club WHERE clubId NOT IN (SELECT clubId FROM club_student WHERE id = '" + stdId + "')";
            ResultSet clubsNotJoined = st.executeQuery(clubsNotJoinedQuery);

            while (clubsNotJoined.next()) {
                clubs.add(clubsNotJoined.getString("name"));
            }
            clubComboBox.getItems().addAll(clubs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStudentValid(String stdId, String stdName) throws Exception{
        IDerrorLabel.setText("");
        Statement st = connections.createStatement();
        String query = "select * from student where id = '"+stdId+"'";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            if(stdName.equals(rs.getString("firstName"))){
                System.out.println("Valid");
                return true;
            }
            else{
                IDerrorLabel.setText("Incorrect student ID/student name combination");
            }
        }
        return false;
    }

    public boolean isStudentRemoved(String studentId, String clubId) {
        System.out.println("Checking if student is removed");
        String selectQuery = "SELECT * FROM removedstudents WHERE studentId = ? AND clubId = ?";

        try (PreparedStatement preparedStatement = connections.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, studentId);
            preparedStatement.setString(2, clubId);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }


    public void registerStudentToClub( ActionEvent event, String studentId, String clubId) throws  Exception{
    String insertQuery = "INSERT INTO club_student (clubId, id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connections.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, clubId);
            preparedStatement.setString(2, studentId);

            preparedStatement.executeUpdate();
            loadStudentDashboard(event);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any exceptions here
        }
    }
    public void loadStudentDashboard(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
        Parent root = loader.load();
        StudentDashboardController SDC = loader.getController();
        SDC.InitializeStudent(student);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}

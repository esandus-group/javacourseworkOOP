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

    //=====================================================================
    public void InitializeStudent(Student std) throws Exception {
        this.student = std;
        this.studentFName = student.getFirstName();
        this.stdId = student.getId();
        setClubsComboBox();         //2.1.1.1. calling the setClubsComboBox method
    }
    //=====================================================================
    public String getClubIdByName(String name) throws SQLException {
        Statement st = connections.createStatement();
        String getClubId = "SELECT * FROM club WHERE name='"+name+"'";
        ResultSet selectedClubIdrs = st.executeQuery(getClubId);
        if(selectedClubIdrs.next()){
            return selectedClubIdrs.getString("clubId");
        }
        return null;
    }
    //=====================================================================
    public void onRegisterToClubButtonClick(ActionEvent event) throws Exception{ //1.1. calling the RegisterToClub method
        String studentId = stdIdTextField.getText();        //1.3 calling the getText method
        String studentName = stdNameTextField.getText();       //1.4 calling the getText method
        String selectedClub = clubComboBox.getValue().toString();        //1.5 calling the getText method

        if (isStudentValid(studentId, studentName)) {       //1.2. calling the isStudentValid checking method

            String clubId = getClubIdByName(selectedClub);
            Club club = getClubByName(selectedClub);
            if (!isStudentRemoved(studentId, clubId)){      //3.1 calling the isStudentRemoved checking method
                student.joinClub(club);                     //3.1.1 calling the joinClub method
                registerStudentToClub(event, studentId, clubId);        //3.1.2 calling the registerStudentToClub method to save to daatabase
            }
        }
    }
    //=====================================================================
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
                student = new Student(id, firstName, lastName, dateOfBirth,password);   //1.5.1 calling the Student constructor
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return student;
    }
    //=====================================================================
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
                club = new Club(clubId, name, studentsPresent);         //2. calling the Student constructor
            }
        }

        return club;
    }
    //=====================================================================
    public void setClubsComboBox() {
        try {
            Statement st = connections.createStatement();
            // Get the clubs that the student has not joined
            String clubsNotJoinedQuery = "SELECT * FROM club WHERE clubId NOT IN (SELECT clubId FROM club_student WHERE id = '" + stdId + "')";
            ResultSet clubsNotJoined = st.executeQuery(clubsNotJoinedQuery);

            while (clubsNotJoined.next()) {
                clubs.add(clubsNotJoined.getString("name"));
            }
            clubComboBox.getItems().addAll(clubs);  //2.1.1.2 calling the getItems().addAll(clubs) method

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //=====================================================================
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
                IDerrorLabel.setText("Incorrect student ID/student name "); //2.1.1.3 calling the setText method
            }
        }
        return false;
    }
    //=====================================================================
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
        IDerrorLabel.setText("Unable to join, as previously removed");  //4. calling the setText method
        return false;
    }

    //=====================================================================
    public void registerStudentToClub( ActionEvent event, String studentId, String clubId) throws  Exception{
    String insertQuery = "INSERT INTO club_student (clubId, id) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connections.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, clubId);
            preparedStatement.setString(2, studentId);

            preparedStatement.executeUpdate();
            loadStudentDashboard(event);
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    //=====================================================================
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
    //=====================================================================
    public void onBackToStudentDashboardClicked(ActionEvent event) throws Exception{
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

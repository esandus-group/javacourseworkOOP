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
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainLoginPageController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text stdNameErrorText;

    @FXML
    private TextField studentIdTextField;
    public String stdId = "Username";
    public String password;
    String fileName;
    Stage stage;

    public Student getStudentWithClubs(String studentId) throws Exception {
        String studentQuery = "SELECT * FROM student WHERE id = ?";
        String clubIdsQuery = "SELECT clubId FROM club_student WHERE id = ?";

        try (PreparedStatement studentStatement = connections.prepareStatement(studentQuery)) {
            studentStatement.setString(1, studentId);
            ResultSet studentResult = studentStatement.executeQuery();

            if (studentResult.next()) {
                String firstName = studentResult.getString("firstName");
                String lastName = studentResult.getString("lastName");
                String dateOfBirth = studentResult.getString("dateOfBirth");
                String password = studentResult.getString("password");

                ArrayList<Club> clubsJoined = new ArrayList<>();

                try (PreparedStatement clubIdsStatement = connections.prepareStatement(clubIdsQuery)) {
                    clubIdsStatement.setString(1, studentId);
                    ResultSet clubIdsResult = clubIdsStatement.executeQuery();

                    while (clubIdsResult.next()) {
                        String clubId = clubIdsResult.getString("clubId");
                        Club club = getClubById(clubId);        //1.2.  calling the method to getClub

                        if (club != null) {
                            clubsJoined.add(club);
                        }
                    }
                }

                return new Student(studentId, firstName, lastName, dateOfBirth, password, clubsJoined);  //4.  calling Student constructor
            }
        }

        return null;
    }
    public Club getClubById(String clubId) throws Exception {
        String clubQuery = "SELECT * FROM Club WHERE clubId = ?";

        try (PreparedStatement clubStatement = connections.prepareStatement(clubQuery)) {
            clubStatement.setString(1, clubId);
            ResultSet clubResult = clubStatement.executeQuery();

            if (clubResult.next()) {
                String name = clubResult.getString("name");

                return new Club(clubId, name);         //2.  calling Club constructor
            }
        }

        return null;
    }

    public void stageLoader(ActionEvent event, String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void onRegisterButtonClick(ActionEvent event) throws IOException{
        fileName = "/SCMS/FxmlFiles/RegisterToSCMS.fxml";
        stageLoader(event, fileName);
    }

    public void Login(ActionEvent event) throws Exception { //1. calling the login to club method
        stdId = studentIdTextField.getText();
        password = passwordTextField.getText();

        if (isStudentValid(stdId, password)){  //1.1.  calling the method to check student validity
            Student student = getStudentWithClubs(stdId);        //1.3.  calling getStudentWithClub method
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

    public boolean isStudentValid(String stdId, String stdPassword) throws Exception{
        Statement st = connections.createStatement();
        String query = "select * from student where id = '"+stdId+"'";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            if(stdPassword.equals(rs.getString("password"))){
                return true;
            }
        }
        stdNameErrorText.setText("Incorrect Student ID/ Password");  //5.1  calling setText method
        return false;
    }

    public void onLoginAsClubAdvisorButtonClick(ActionEvent event) throws IOException {
        fileName = "/SCMS/FxmlFiles/ClubLoginPage.fxml";

        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

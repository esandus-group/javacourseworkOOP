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
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
public class StudentDashboardController {
    ArrayList<String> clubs = new ArrayList<>();
    public String studentId;
    @FXML
    private Text welcomeText;
    String club1name = null;
    String club2name = null;
    String club3name = null;

    @FXML
    private Button club1Button;

    @FXML
    private Button club2Button;
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB
    @FXML
    private Button club3Button;
    private Student student;
    String buttonText;

    Stage stage;

    public void getRegisteredClubs() throws Exception {
        welcomeText.setText(student.getFirstName());
        try (
             Statement st = connections.createStatement()) {

            String studentIdQuery = "SELECT id FROM student WHERE firstName = '" + student.getFirstName() + "'";
            ResultSet studentIdResult = st.executeQuery(studentIdQuery);
            if (studentIdResult.next()) {
                studentId = studentIdResult.getString("id");
                String studentClubQuery = "SELECT clubId FROM club_student WHERE id = '" + studentId + "'";
                ResultSet studentClubResult = st.executeQuery(studentClubQuery);
                int clubCount = 0;
                while(studentClubResult.next()){
                    clubs.add(studentClubResult.getString("clubId"));
                }
                club1Button.setVisible(false);
                club2Button.setVisible(false);
                club3Button.setVisible(false);
                for(String clubId:clubs){
                    String clubQuery = "SELECT name FROM club WHERE clubId = '" + clubId + "'";
                    ResultSet clubResult = st.executeQuery(clubQuery);
                    if (clubResult.next()) {
                        String clubName = clubResult.getString("name");
                        clubCount++;

                        if (clubCount == 1) {
                            club1Button.setVisible(true);
                            club1name = clubName;
                            club1Button.setText(club1name);
                        } else if (clubCount == 2) {
                            club2Button.setVisible(true);
                            club2name = clubName;
                            club2Button.setText(club2name);
                        } else if (clubCount == 3) {
                            club3Button.setVisible(true);
                            club3name = clubName;
                            club3Button.setText(club3name);
                        }
                    }
                }
            }
        }
    }
    public void InitializeStudent(Student std) throws Exception {
        this.student = std;
        getRegisteredClubs();

    }



    public void onRegisterClubButtonClick(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/RegisterToClubs.fxml"));
        Parent root = loader.load();
        RegisterToClubController RTC = loader.getController();
        RTC.InitializeStudent(student);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void onClub1ButtonClick(ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/ClubDashboard.fxml"));
        Parent root = loader.load();
        buttonText = club1Button.getText();
        ClubDashboardController CDC = loader.getController();
        CDC.InitializeStudent(buttonText, student);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void onClub2ButtonClick(ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/ClubDashboard.fxml"));
        Parent root = loader.load();
        buttonText = club2Button.getText();
        ClubDashboardController CDC = loader.getController();
        CDC.InitializeStudent(buttonText, student);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void onClub3ButtonClick(ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/ClubDashboard.fxml"));
        Parent root = loader.load();
        buttonText = club3Button.getText();
        ClubDashboardController CDC = loader.getController();
        CDC.InitializeStudent(buttonText, student);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}

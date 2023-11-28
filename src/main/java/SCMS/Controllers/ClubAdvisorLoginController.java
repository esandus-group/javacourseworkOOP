package SCMS.Controllers;

import SCMS.HelloApplication;
import SCMS.Objects.Club;
import SCMS.Objects.ClubAdvisor;
import SCMS.Utils.SCMSEnvironment;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClubAdvisorLoginController {   //(FULLY DONE BY ESANDU)
    @FXML
    private Label idStatus;

    @FXML
    private Label passwordStatus;
    @FXML
    private TextField AdvisorIdTextField;

    @FXML
    private Button advisorLoginButton;
    @FXML
    private TextField passwordTextField;
    HelloApplication h1 = new HelloApplication();
    @FXML
    private Button signUpAdvisor;
    public String adId;

    public String password;
    //=========================================================================
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB
    //=========================================================================
    @FXML
    public void backToLoginPage(ActionEvent event) throws IOException { //the back button
        String fileName = "/SCMS/FxmlFiles/MainLoginPage.fxml";
        h1.stageLoader(event,fileName);
    }

    //=========================================================================
    //the method to return the club advisor object from the database
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
    //=========================================================================
    public boolean isInputNotNull(String input){
        if ( input== null || input.equals("")){
            return false;
        }
        return true;
    }
    //=========================================================================
    public void Login(ActionEvent event) throws Exception {
            adId = AdvisorIdTextField.getText();
            password = passwordTextField.getText();
            ClubAdvisor advisor = getClubAdvisor(adId);

        if (!isInputNotNull(adId)){
            idStatus.setText("pls enter the Teacher Id");
            return;
        }
        if (!isInputNotNull(password)){
            passwordStatus.setText("pls enter the password");
            return;
        }
            if (advisor==null){
                passwordStatus.setText("Invalid Id entered");
                return;
            }
            if (!advisor.getPassword().equals(password)){
                passwordStatus.setText("Password is incorrect");
                return;
            }

            else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
                Parent root = loader.load();

                clubAdvisorController cac = loader.getController();
                cac.setWelcomeText(advisor);
                cac.gettingAdvisorFromPressClubCon(advisor);

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            }
    }

    //=========================================================================
    public void onRegisterClubAdvisorButtonClick(ActionEvent event) throws IOException{

        Parent root = FXMLLoader.load(getClass().getResource("/SCMS/FxmlFiles/ClubAdvisorSignUp.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}

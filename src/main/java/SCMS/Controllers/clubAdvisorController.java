package SCMS.Controllers;

import SCMS.HelloApplication;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

//nikoya is here
public class clubAdvisorController {
    HelloApplication helloApplication = new HelloApplication();
    @FXML
    private Button backButtonCDD;
    public String fileName;
    @FXML
    private Button addclub;
    private ArrayList<String> clubs = new ArrayList<>();
    @FXML
    private Button club1;

    @FXML
    private Button club2;

    @FXML
    private Button club3;
    String club1name = null;
    String club2name = null;
    String club3name = null;
    String club4name = null;
    @FXML
    private Button club4;
    private String buttonText;
    private String advisorID;
    public ClubAdvisor advisor;
    @FXML
    private Label welcome;
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //getting the database connection
    public void setWelcomeText(String name,ClubAdvisor advisor) throws Exception{
        welcome.setText("Welcome "+name);
        this.advisor=advisor;
        getManagedClubs(advisor.getId());
    }
    public void getManagedClubs(String advisorId) throws Exception {
        try (Statement st = connections.createStatement()) {

            String advisorClubQuery = "SELECT clubId FROM club WHERE idOfAdvisor = '" + advisorId + "'";
            ResultSet advisorClubResult = st.executeQuery(advisorClubQuery);

            int clubCount = 0;
            while (advisorClubResult.next()) {
                clubs.add(advisorClubResult.getString("clubId"));
            }
            for(String club: clubs){
                System.out.println(club);
            }

            club1.setVisible(false);
            club2.setVisible(false);
            club3.setVisible(false);
            club4.setVisible(false);

            for (String clubId : clubs) {
                String clubQuery = "SELECT name FROM club WHERE clubId = '" + clubId + "'";
                ResultSet clubResult = st.executeQuery(clubQuery);

                if (clubResult.next()) {
                    String clubName = clubResult.getString("name");
                    System.out.println(clubName);
                    clubCount++;

                    if (clubCount == 1) {
                        club1.setVisible(true);
                        club1name = clubName;
                        club1.setText(club1name);
                    } else if (clubCount == 2) {
                        club2.setVisible(true);
                        club2name = clubName;
                        club2.setText(club2name);
                    } else if (clubCount == 3) {
                        club3.setVisible(true);
                        club3name = clubName;
                        club3.setText(club3name);
                    }
                    else if (clubCount == 4) {
                        club4.setVisible(true);
                        club4name = clubName;
                        club4.setText(club4name);
                    }
                }
            }
        }
    }
    //=============================================================

    //---------------------------------------------------------------------
    public void onAddClubClick(ActionEvent event) throws Exception{ // add club
        fileName="/SCMS/FxmlFiles/CreateClub.fxml";      //the fxml path
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        buttonText = club1.getText();

        createClubController pcc = loader.getController();
        pcc.gettingIdOfAdvisor(advisorID);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void loadingTheClubDashBoard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/PressClub.fxml"));
        Parent root = loader.load();
        buttonText = club1.getText();
        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText,advisorID);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void onClub1PressClick(ActionEvent event) throws  IOException{
        loadingTheClubDashBoard(event);
    }
    public void onClub2PressClick(ActionEvent event) throws  IOException{
        loadingTheClubDashBoard(event);
    }
    public void onClub3PressClick(ActionEvent event) throws  IOException{
        loadingTheClubDashBoard(event);
    }
    //---------------------------------------------------------------------
    public void onClub4PressClick(ActionEvent event) throws  IOException{
        loadingTheClubDashBoard(event);
    }
    //=========================================================
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/ClubLoginPage.fxml";      //open the page
        helloApplication.stageLoader(event,fileName);
    }

}

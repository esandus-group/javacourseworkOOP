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
import java.sql.*;
import java.util.ArrayList;

public class clubAdvisorController {         //(FULLY DONE BY ESANDU)
    HelloApplication helloApplication = new HelloApplication(); //creating the instance
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
    public ClubAdvisor currentAdvisor;
    //=====================================================================
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //getting the database connection
    //=====================================================================
    //to get the information from the previous controller
    public void gettingAdvisorFromPressClubCon(ClubAdvisor advisor){
        this.currentAdvisor=advisor;
        this.advisor=currentAdvisor;
    }
    //=====================================================================
    //to set the label names of the page when loading
    public void setWelcomeText(ClubAdvisor advisor) throws Exception{
        String name = advisor.getFirstName();
        welcome.setText("Welcome "+name);
        this.advisor=advisor;
        getManagedClubs(advisor.getId());
    }
    //=====================================================================
    //setting the club names for the club buttons
    public void getManagedClubs(String advisorId) throws Exception {
        try (Statement st = connections.createStatement()) {

            //to get the clubs that the advisor's clubs from the database
            String advisorClubQuery = "SELECT clubId FROM club WHERE idOfAdvisor = '" + advisorId + "'";
            ResultSet advisorClubResult = st.executeQuery(advisorClubQuery);

            int clubCount = 0;
            while (advisorClubResult.next()) {
                clubs.add(advisorClubResult.getString("clubId"));
            }
            club1.setVisible(false);
            club2.setVisible(false);
            club3.setVisible(false);
            club4.setVisible(false);


            for (String clubId : clubs) {
                String clubQuery = "SELECT name FROM club WHERE clubId = '" + clubId + "'";
                ResultSet clubResult = st.executeQuery(clubQuery);

                //getting the name of those clubs
                if (clubResult.next()) {
                    String clubName = clubResult.getString("name");
                    System.out.println(clubName);
                    clubCount++;

                    //making the buttons visible, and putting the name
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

    public void onAddClubClick(ActionEvent event) throws Exception{ // add club
        fileName="/SCMS/FxmlFiles/CreateClub.fxml";      //the fxml path
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName)); //opening the fxml
        Parent root = loader.load();
        System.out.println(advisor.getId());
        createClubController pcc = loader.getController();
        pcc.gettingIdOfAdvisor(advisor.getId());


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //=====================================================================
    //the method to open the fxml for club dashboard when advisor clicks club
    public void loadingTheClubDashBoard(ActionEvent event,Button club) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/PressClub.fxml"));
        Parent root = loader.load();
        buttonText = club.getText();

        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText,advisor.getId());

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //=====================================================================
    public void onClub1PressClick(ActionEvent event) throws IOException, SQLException {
        loadingTheClubDashBoard(event,club1); //also passing which button it is also
    }
    public void onClub2PressClick(ActionEvent event) throws  IOException, SQLException {
        loadingTheClubDashBoard(event,club2); //also passing which button it is also
    }
    public void onClub3PressClick(ActionEvent event) throws  IOException, SQLException {
        loadingTheClubDashBoard(event,club3); //also passing which button it is also
    }
    //---------------------------------------------------------------------
    public void onClub4PressClick(ActionEvent event) throws  IOException, SQLException {
        loadingTheClubDashBoard(event,club4); //also passing which button it is also
    }
    //=========================================================
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/ClubLoginPage.fxml";      //open the page
        helloApplication.stageLoader(event,fileName);
    }

}

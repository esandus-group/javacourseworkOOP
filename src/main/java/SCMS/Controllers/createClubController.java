 package SCMS.Controllers;

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
import java.sql.*;

public class createClubController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //getting the database connection
    @FXML
    private TextField clubName;
    @FXML
    private TextField clubAdvisorID;

    public String clubAdvisorId;
    @FXML
    private Label creationStatus;
    @FXML
    private Label idStatus;

    @FXML
    private Label nameStatus;
    ClubAdvisor currentClubAdvisor = null;

    public String clubId;
    public String clubNam;
    public String idOfAdvisor;
    @FXML
    private Button backButtonCDD;

    public void gettingIdOfAdvisor(String id){
        this.idOfAdvisor=id;
    }

    public void stageLoader(ActionEvent event, String fileName) throws IOException { //STAGE LOADER METHOD
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
        Parent root = loader.load();

        //passing the advisorID to the next controller and also setting the name
        clubAdvisorController cac = loader.getController();
        cac.setWelcomeText(getClubAdvisor(idOfAdvisor).getFirstName(), idOfAdvisor);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    //=========================================================================
    public String getNewClubId() { //we count how many rows are there and add 1 to get the new id
        //generating new club id

        int newClubId = 0;
        String countQuery = "SELECT COUNT(*) FROM Club";

        try (PreparedStatement countStatement = connections.prepareStatement(countQuery)) {
            ResultSet countResult = countStatement.executeQuery();
            if (countResult.next()) {
                newClubId = countResult.getInt(1) + 1; // Increment the count by 1 to get the new club ID
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

        return Integer.toString(newClubId);
    }

    //=========================================================================
    public ClubAdvisor getClubAdvisor(String clubAdvisorId) throws SQLException {

        String query = "SELECT * FROM ClubAdvisor WHERE id = ?"; //this is the query

        try (PreparedStatement statement = this.connections.prepareStatement(query)) {
            statement.setString(1, clubAdvisorId);
            ResultSet resultSet = statement.executeQuery();  //resultset is used to store the data from DB
            if (resultSet.next()) {
                String id = resultSet.getString("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String dateOfBirth = resultSet.getString("dateOfBirth");
                String password = resultSet.getString("password");

                return new ClubAdvisor(id, firstName, lastName, dateOfBirth, password); // Create a new ClubAdvisor object
            }
        }
        return null;
    }

    //=========================================================================
    public boolean doesClubExists(String clubName) { //this method checks whether that club exists
        String query = "SELECT COUNT(*) FROM Club WHERE name = ?";

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, clubName);//i will check
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            int count = resultSet.getInt(1);

            return count > 0; //that means if it's greater than 0 , that name is there because a row was counted
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean isAdvisorIdValid(String id){
        if ( id== null || id.equals("")){
            return false;
        }
        return true;
    }

    public boolean isClubNameValid(String name){
        if ( name== null || name.equals("")){
            return false;
        }
        return true;
    }
    //=========================================================================

    public void onSaveNewClubClick(ActionEvent event) throws Exception {
        clubNam = clubName.getText();                               //getting the information
        clubAdvisorId = clubAdvisorID.getText();                       //from the text fields

        //========================================(validation)======

        if (!isClubNameValid(clubNam)){ // checking whether tha name is empty
            nameStatus.setText("pls enter the name of the club");
        }

        if (!isAdvisorIdValid(clubAdvisorId)){ //checking whether the ID is empty
            idStatus.setText("pls enter the id of the advisor");
            return;
        }

        //checking whether the id exists
        if (getClubAdvisor(clubAdvisorId) == null) {
            idStatus.setText("such a ID not found, re enter");
        } else {
            currentClubAdvisor = getClubAdvisor(clubAdvisorId); //storing the club advisor to create the club

            if (doesClubExists(clubNam)) {   //checking whether a club with that name exists

                nameStatus.setText("A club with this name already exists.");

            } else {

                clubId = getNewClubId(); //getting the new club id
                Club newClub = new Club(clubId, clubNam, clubAdvisorId);
                currentClubAdvisor.addClub(newClub);
                if (newClub != null) {
                    System.out.println("Club created successfully.");
                    addClubToDatabase(newClub);

                    clubName.setText(""); //clearing the text fields and the labels
                    clubAdvisorID.setText("");
                    nameStatus.setText("");
                    idStatus.setText("");
                } else {
                    System.out.println("Failed to create the club.");
                }
            }
        }
    }
    //=========================================================================

    private void addClubToDatabase(Club newClub) {

        String query = "INSERT INTO Club (clubId, name, idOfAdvisor) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, newClub.getClubId());
            statement.setString(2, newClub.getName());
            statement.setString(3, newClub.getIdOfAdvisor());

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Club added to the database.");
            } else {
                System.out.println("Failed to add the club to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    //====================================================================
}

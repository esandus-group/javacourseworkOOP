package SCMS.Controllers;

import SCMS.Objects.Club;
import SCMS.Objects.ClubAdvisor;
import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    public int clubId;
    public String clubNam;





    //=========================================================================
    public int getNewClubId() { //we count how many rows are there and add 1 to get the new id
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

        return newClubId;
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
        return null; // ClubAdvisor not found
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
                Club newClub = currentClubAdvisor.createClub(clubId, clubNam, clubAdvisorId);
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
        // Create an SQL statement to add the new club to the database
        String query = "INSERT INTO Club (clubId, name, idOfAdvisor) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setInt(1, newClub.getClubId());
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

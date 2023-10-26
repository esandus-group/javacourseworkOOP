package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class createClubController {
    @FXML
    private TextField clubName;
    @FXML
    private TextField clubAdvisorID;

    public String clubAdvisorId;

    @FXML
    private Label idStatus;

    @FXML
    private Label nameStatus;
    ClubAdvisor currentClubAdvisor = null;

    public int clubId;
    public String clubNam;

    private Connection connections;


    //=========================================================================
    public createClubController() {
        try {
            // Initialize the database connection when the controller is created
            String url = "jdbc:mysql://localhost:3306/school_club_management";
            String username = "root";
            String password = "esandu12345";
            Class.forName("com.mysql.cj.jdbc.Driver");
            connections = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception appropriately
        }
    }
    //=========================================================================
    public int getNewClubId() {
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
        String query = "SELECT * FROM ClubAdvisor WHERE id = ?";
        try (PreparedStatement statement = this.connections.prepareStatement(query)) { // Use the instance variable
            statement.setString(1, clubAdvisorId);
            ResultSet resultSet = statement.executeQuery();
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
    public boolean isClubExists(String clubName) {
        String query = "SELECT COUNT(*) FROM Club WHERE name = ?";
        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, clubName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception here
            return false; // In this case, return false to indicate that an error occurred
        }
    }
    //=========================================================================

    public void onSaveNewClubClick(ActionEvent event) throws Exception {
        clubNam = clubName.getText();                               //getting the information
        clubAdvisorId = clubAdvisorID.getText();

        if (getClubAdvisor(clubAdvisorId) == null) {

            idStatus.setText("suck a ID not found, re enter");
        } else {
            currentClubAdvisor = getClubAdvisor(clubAdvisorId);
            if (isClubExists(clubNam)) {

                nameStatus.setText("A club with this name already exists.");

            } else {
                clubId = getNewClubId();
                Club newClub = currentClubAdvisor.createClub(clubId, clubNam, clubAdvisorId);
                if (newClub != null) {
                    System.out.println("Club created successfully.");
                    addClubToDatabase(newClub);
                    clubName.setText("");
                    clubAdvisorID.setText("");
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
            // Handle the exception here
        }
    }
    //====================================================================
}

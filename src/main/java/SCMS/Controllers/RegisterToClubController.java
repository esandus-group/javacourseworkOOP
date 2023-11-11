package SCMS.Controllers;

import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

//Join Club is fully functional
public class RegisterToClubController {
//Load data to the 2 array lists.
    ArrayList<String> studentNames = new ArrayList<>();


    ArrayList<String> clubsIdNotJoined = new ArrayList<>();
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
    String selectedClubId;

    public void onRegisterToClubButtonClick(ActionEvent event) throws Exception{
        String studentId = stdIdTextField.getText();
        String studentName = stdNameTextField.getText();
        String selectedClub = clubComboBox.getValue().toString();
        Statement st = connections.createStatement();
        String getClubId = "SELECT * FROM club WHERE name='"+selectedClub+"'";
        ResultSet selectedClubIdrs = st.executeQuery(getClubId);
        if(selectedClubIdrs.next()){
            selectedClubId = selectedClubIdrs.getString("clubId");
        }

        if (isStudentValid(studentId, studentName)) {
            if (!isStudentRemoved(studentId, selectedClubId)){
                System.out.println("Nigga");
                registerStudentToClub(event, studentId, selectedClubId);
            }

        }
    }
    public void setStudentName(String name, String stdId){
        this.studentFName = name;
        this.stdId = stdId;
        setClubsComboBox();
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

            // Check for clubs without members
            String clubsWithoutMembersQuery = "SELECT * FROM club WHERE clubId NOT IN (SELECT clubId FROM club_student)";
            ResultSet clubsWithoutMembers = st.executeQuery(clubsWithoutMembersQuery);

            while (clubsWithoutMembers.next()) {
                clubs.add(clubsWithoutMembers.getString("name"));
            }

            // Display the clubs in the combo box
            for (String club : clubs) {
                System.out.println(club);
            }
            clubComboBox.getItems().addAll(clubs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isStudentValid(String stdId, String stdName) throws Exception{
        System.out.println(stdId+"."+stdName);
        Statement st = connections.createStatement();
        String query = "select * from student where id = '"+stdId+"'";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            System.out.println(rs.getString("firstName")+"Before");
            if(stdName.equals(rs.getString("firstName"))){
                System.out.println("Valid");
                return true;
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
            System.out.println("Data inserted into ApproveStudents table successfully.");
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
        SDC.setWelcomeText(studentFName);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}

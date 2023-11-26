package SCMS.Controllers;
import SCMS.Objects.ClubAdvisor;
import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class ClubAdvisorSignupController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB


    @FXML
    private DatePicker stdDOBDatePicker;

    @FXML
    private TextField stdFirstNameTextField;

    @FXML
    private TextField stdIdTextField;

    @FXML
    private TextField stdLastNameTextField;

    @FXML
    private TextField stdPasswordTextField;

    @FXML
    private Text dOBErrorText;

    @FXML
    private Text fNameErrorText;

    @FXML
    private Text idErrorText;
    @FXML
    private Text TidErrorText;
    @FXML
    private TextField TeacherIdTextField;

    @FXML
    private Text lNameErrorText;

    @FXML
    private Text pwErrorText;
    String advisorId;
    String teacherID;
    String advisorFName;
    String advisorLName;
    
    String stdDOB;
    String stdPassword;
    public boolean isInputEmpty(String input){
        if ( input== null || input.equals("")){
            return false;
        }
        return true;
    }

    public boolean isTeacherIdValid(String teacherId) throws SQLException {
        String query = "SELECT * FROM Teacher WHERE teacherId = ?";

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, teacherId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Returns true if the teacher ID exists, false otherwise
            }
        }
    }
    public boolean advisorInputValidator() throws Exception {
        if (!isInputEmpty(teacherID)){
            TidErrorText.setText("pls enter the Teacher Id");
            return false;
        }
        if (!isInputEmpty(advisorId)){
            idErrorText.setText("pls enter the advisor Id");
            return false;
        }
        if (!isInputEmpty(advisorFName)){
            fNameErrorText.setText("pls enter the First name");
            return false;
        }
        if (!isInputEmpty(advisorLName)){
            lNameErrorText.setText("pls enter the Last name");
            return false;
        }
        if (!isInputEmpty(stdDOB)){
            dOBErrorText.setText("pls enter the date of Birth");
            return false;
        }
        if (!isInputEmpty(stdPassword)){
            TidErrorText.setText("pls enter the Password");
            return false;
        }
        if (stdPassword.length() < 6) {
            pwErrorText.setText("Password must be at least 6 characters long");
            return false;
        }
        //checking whether the teacher id is correct
        if (!isTeacherIdValid(teacherID)){
            TidErrorText.setText("Invalid id entered, pls re try");
            return false ;
        }
        return true;
    }
    public void savingClubAdvisor(ClubAdvisor newAdvisor) throws SQLException {
        String insertAdvisorQuery = "INSERT INTO ClubAdvisor (id, firstName, lastName, dateOfBirth, password) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement insertAdvisorStatement = this.connections.prepareStatement(insertAdvisorQuery)) {
            insertAdvisorStatement.setString(1, newAdvisor.getId());
            insertAdvisorStatement.setString(2, newAdvisor.getFirstName());
            insertAdvisorStatement.setString(3, newAdvisor.getLastName());
            insertAdvisorStatement.setString(4, newAdvisor.getDateOfBirth());
            insertAdvisorStatement.setString(5, newAdvisor.getPassword());

            insertAdvisorStatement.executeUpdate();
        }
    }

    public void onRequestToJoinButtonClick(ActionEvent event) throws Exception{
        teacherID =  TeacherIdTextField.getText();
        advisorId = stdIdTextField.getText();
        advisorFName = stdFirstNameTextField.getText();
        advisorLName = stdLastNameTextField.getText();
        stdDOB = String.valueOf(stdDOBDatePicker.getValue());
        stdPassword = stdPasswordTextField.getText();

        //validation done
        if (advisorInputValidator()){
            //finally creating the object
            ClubAdvisor advisor = new ClubAdvisor(advisorId,advisorFName,advisorLName,stdDOB,stdPassword);
            advisor.displayInfo();
            savingClubAdvisor(advisor);
            System.out.println("done");
            TidErrorText.setText("");
            pwErrorText.setText("");
            dOBErrorText.setText("");
            lNameErrorText.setText("");
            fNameErrorText.setText("");
            idErrorText.setText("");

            //loading the dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/Club advisor.fxml"));
            Parent root = loader.load();
            //passing the advisorID to the next controller and also setting the name
            clubAdvisorController cac = loader.getController();
            cac.setWelcomeText(advisor);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }


    }
}

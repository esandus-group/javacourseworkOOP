package SCMS.Controllers;

import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegisterToSCMSController {

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
    private Text lNameErrorText;

    @FXML
    private Text pwErrorText;
    String stdId;
    String stdFName;
    String stdLName;
    String stdDOB;
    String stdPassword;

    @FXML
    void onRequestToJoinButtonClick(ActionEvent event) throws Exception {
        Statement st = connections.createStatement();
        stdId = stdIdTextField.getText();
        stdFName = stdFirstNameTextField.getText();
        stdLName = stdLastNameTextField.getText();
        stdDOB = String.valueOf(stdDOBDatePicker.getValue());
        stdPassword = stdPasswordTextField.getText();

        if(isStudentIdValid(stdId) && isStudentFNameValid(stdFName) && isStudentLNameValid(stdLName) && isStudentDOBValid(stdDOB) && isStudentPasswordValid(stdPassword)){
            String insertQuery = "INSERT INTO student (id, firstName, lastName, dateOfBirth, password) VALUES (?, ?, ?, ?, ?)";
            System.out.println(stdId+stdFName+stdLName+stdDOB+stdPassword);
            try (PreparedStatement preparedStatement = connections.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, stdId);
                preparedStatement.setString(2, stdFName);
                preparedStatement.setString(3, stdLName);
                preparedStatement.setString(4, stdDOB);
                preparedStatement.setString(5, stdPassword);

                preparedStatement.executeUpdate();
                System.out.println("Data inserted into Students table successfully.");
                loadStudentDashboard(event);
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle any exceptions here
            }
        }
    }
    public boolean isStudentIdValid(String studentId) throws Exception {
        idErrorText.setText("");
        if (studentId != null) {
            // Check if the student ID follows the format "S" followed by numbers
            if (studentId.matches("S\\d+")) {
                // Check if the student ID already exists in the database
                String query = "SELECT * FROM student WHERE id = ?";
                try (PreparedStatement preparedStatement = connections.prepareStatement(query)) {
                    preparedStatement.setString(1, studentId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    return !resultSet.next(); // Returns true if the result set is empty, indicating that the student ID is not in use
                }
            }
            idErrorText.setText("Invalid format");
            return false;
        }
        idErrorText.setText("Id cannot be null");
        return false;
    }

    public boolean isStudentFNameValid(String studentFName) throws Exception{
        fNameErrorText.setText("");
        if(!studentFName.equals("")){
            System.out.println("true");
            return true;
        }
        fNameErrorText.setText("First name cannot be null");
        return false;
    }
    public boolean isStudentLNameValid(String studentLName) throws Exception{
        lNameErrorText.setText("");
        if(!studentLName.equals("")){
            return true;
        }
        lNameErrorText.setText("Last name cannot be null");
        return false;
    }
    public boolean isStudentDOBValid(String studentDOB) throws Exception {
        dOBErrorText.setText("");
        System.out.println(studentDOB);
        if (!studentDOB.equals("")) {
            System.out.println("DOB not null");
            LocalDateTime selectedDate = LocalDateTime.parse(studentDOB + "T00:00:00");
            LocalDateTime currentDate = LocalDateTime.now();
            if(!selectedDate.isAfter(currentDate)){
                return true;
            }
            dOBErrorText.setText("Date of birth cannot be in the future");
            return false;
        }
        dOBErrorText.setText("Date of birth cannot be null");
        return false;
    }
    public boolean isStudentPasswordValid(String studentPassword) throws Exception{
        pwErrorText.setText("");
        if(!studentPassword.equals("")){
            return true;
        }
        pwErrorText.setText("Password cannot be null");
        return false;
    }
    public void loadStudentDashboard(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
        Parent root = loader.load();
        StudentDashboardController SDC = loader.getController();
        SDC.setWelcomeText(stdFName);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



}
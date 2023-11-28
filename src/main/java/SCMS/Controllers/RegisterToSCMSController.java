package SCMS.Controllers;

import SCMS.HelloApplication;
import SCMS.Objects.Student;
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
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class RegisterToSCMSController { //(FULLY DONE BY RANIDU)

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
    HelloApplication h1 = new HelloApplication();
    @FXML
    private Text pwErrorText;
    String stdId;
    String stdFName;
    String stdLName;
    String stdDOB;
    String stdPassword;
    //=====================================================================
    public boolean studentInputValidator() throws Exception {
//      Validating the student details.
        if(isStudentIdValid(stdId) && isStudentFNameValid(stdFName) && isStudentLNameValid(stdLName) && isStudentDOBValid(stdDOB) && isStudentPasswordValid(stdPassword)){
            return true;
        }
        return false;
    }
    //=====================================================================
    public void saveNewStudentToDatabase(){
//      Adding student to the database
        String insertQuery = "INSERT INTO student (id, firstName, lastName, dateOfBirth, password) VALUES (?, ?, ?, ?, ?)";
        System.out.println(stdId+stdFName+stdLName+stdDOB+stdPassword);
        try (PreparedStatement preparedStatement = connections.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, stdId);
            preparedStatement.setString(2, stdFName);
            preparedStatement.setString(3, stdLName);
            preparedStatement.setString(4, stdDOB);
            preparedStatement.setString(5, stdPassword);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //=====================================================================
    public void backToLoginPage(ActionEvent event) throws IOException {
        String fileName = "/SCMS/FxmlFiles/MainLoginPage.fxml";
        h1.stageLoader(event,fileName);
    }
    //=====================================================================
    @FXML
    void onRequestToJoinButtonClick(ActionEvent event) throws Exception {       //1. calling the method to register
        Statement st = connections.createStatement();
        stdId = stdIdTextField.getText();
        stdFName = stdFirstNameTextField.getText();
        stdLName = stdLastNameTextField.getText();
        stdDOB = String.valueOf(stdDOBDatePicker.getValue());
        stdPassword = stdPasswordTextField.getText();

        if(studentInputValidator()){        //1.1. calling the method to validate student infor

            Student student= new Student(stdId, stdFName, stdLName, stdDOB, stdPassword);          //2. calling the Student constructor

            saveNewStudentToDatabase();      //3.1. method to save to the database

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
            Parent root = loader.load();
            StudentDashboardController SDC = loader.getController();
            SDC.InitializeStudent(student);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        }
    }
    //=====================================================================
    public boolean isStudentIdValid(String studentId) throws Exception {
        idErrorText.setText("");
        if (studentId != null) {
                // Check if the student ID already exists in the database
                String query = "SELECT * FROM student WHERE id = ?";
                try (PreparedStatement preparedStatement = connections.prepareStatement(query)) {
                    preparedStatement.setString(1, studentId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    idErrorText.setText("That Id already exists");
                    return !resultSet.next(); // Returns true if the result set is empty, indicating that the student ID is not in use
                }
        }
        idErrorText.setText("Id cannot be null");       //3.2. method to setText
        return false;
    }
    //=====================================================================
    public boolean isStudentFNameValid(String studentFName) throws Exception{
        fNameErrorText.setText("");
        if(!studentFName.equals("")){
            System.out.println("true");
            return true;
        }
        fNameErrorText.setText("First name cannot be null");    //3.2. method to setText
        return false;
    }
    //=====================================================================
    public boolean isStudentLNameValid(String studentLName) throws Exception{
        lNameErrorText.setText("");
        if(!studentLName.equals("")){
            return true;
        }
        lNameErrorText.setText("Last name cannot be null");      //3.2. method to setText
        return false;
    }
    //=====================================================================
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
            dOBErrorText.setText("Date of birth cannot be in the future");      //3.2. method to setText
            return false;
        }
        dOBErrorText.setText("Date of birth cannot be null");           //3.2. method to setText
        return false;
    }
    //=====================================================================
    public boolean isStudentPasswordValid(String studentPassword) throws Exception{
        pwErrorText.setText("");
        if(!studentPassword.equals("")){
            return true;
        }
        pwErrorText.setText("Password cannot be null");     //3.2. method to setText
        return false;
    }
    //=====================================================================



}
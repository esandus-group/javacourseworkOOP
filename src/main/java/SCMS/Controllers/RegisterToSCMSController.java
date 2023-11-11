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
    ArrayList<String> allStudentId = new ArrayList<>();

    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private Button requestToJoinButton;

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
    public boolean isStudentIdValid(String studentId) throws Exception{
        return studentId != null;
    }
    public boolean isStudentFNameValid(String studentFName) throws Exception{
        return studentFName != null;
    }
    public boolean isStudentLNameValid(String studentFName) throws Exception{
        return studentFName != null;
    }
    public boolean isStudentDOBValid(String studentDOB) throws Exception{
        return studentDOB != null;
    }    public boolean isStudentPasswordValid(String studentPassword) throws Exception{
        return studentPassword != null;
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
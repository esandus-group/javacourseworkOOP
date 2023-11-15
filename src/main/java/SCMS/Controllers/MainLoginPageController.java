package SCMS.Controllers;

import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainLoginPageController {
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text stdNameErrorText;

    @FXML
    private TextField studentIdTextField;
    public String stdId = "Username";
    public String password;
    String fileName;
    Stage stage;

    public void stageLoader(ActionEvent event, String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void onRegisterButtonClick(ActionEvent event) throws IOException{
        fileName = "/SCMS/FxmlFiles/RegisterToSCMS.fxml";
        stageLoader(event, fileName);
    }
    public String getStudentName(String stdId) throws Exception{
        Statement st = connections.createStatement();
        String query = "select * from student where id = '"+stdId+"'";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            return rs.getString("firstName");

        }
        return "Student Not Found";
    }
    public void Login(ActionEvent event) throws Exception {
        stdId = studentIdTextField.getText();
        password = passwordTextField.getText();
        String stdName = getStudentName(stdId);
        if (isStudentValid(stdId, password)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
            Parent root = loader.load();
            StudentDashboardController SDC = loader.getController();
            SDC.setWelcomeText(stdName);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }

    }
    public boolean isStudentValid(String stdId, String stdPassword) throws Exception{
        Statement st = connections.createStatement();
        String query = "select * from student where id = '"+stdId+"'";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            if(stdPassword.equals(rs.getString("password"))){
                return true;
            }
        }
        stdNameErrorText.setText("Incorrect Student ID/ Password Combination");
        return false;
    }
    public void onLoginAsClubAdvisorButtonClick(ActionEvent event) throws IOException {
        fileName = "/SCMS/FxmlFiles/ClubLoginPage.fxml";
        stageLoader(event, fileName);
    }
}

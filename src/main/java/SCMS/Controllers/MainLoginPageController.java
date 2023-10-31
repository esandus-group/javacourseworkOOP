package SCMS.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MainLoginPageController {

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text stdNameErrorText;

    @FXML
    private Text passwordErrorText;
    @FXML
    private Button cllubAdvisorLoginButton;
    @FXML
    private TextField studentNameTextField;
    public String username = "Username";
    public String password;
    public static String fileName;
    Stage stage;

    public MainLoginPageController(){

    }
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
    public void onClubAdvisorLoginButtonClick(ActionEvent event) throws IOException{
        fileName = "/SCMS/FxmlFiles/Club advisor.fxml";
        stageLoader(event, fileName);
    }
    public void Login(ActionEvent event) throws IOException{
        fileName = "/SCMS/FxmlFiles/StudentDashboard.fxml";
        if (validateUsername() && validatePassword()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SCMS/FxmlFiles/StudentDashboard.fxml"));
            Parent root = loader.load();
            StudentDashboardController SDC = loader.getController();
            SDC.setWelcomeText(username);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
    public boolean validateUsername(){
        username = studentNameTextField.getText();
        stdNameErrorText.setText(" ");
        if(username.equals("")){
            stdNameErrorText.setText("Please enter your name");
            return false;
        }
        return true;
    }
    public boolean validatePassword(){
        password = passwordTextField.getText();
        passwordErrorText.setText(" ");
        if(password.equals("")){
            passwordErrorText.setText("Please enter your password");
            return false;
        }
        if(!password.equals("studentPassword")){
            passwordErrorText.setText("Incorrect password");
            return false;
        }

        return true;
    }


}

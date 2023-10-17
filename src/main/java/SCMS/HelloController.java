package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Text passwordErrorText;
    @FXML
    private Text usernameErrorText;

    @FXML
    private Text welcomeText;

    @FXML
    private TextField passwordTextField;

    @FXML
    private Button studentLoginButton;

    @FXML
    private TextField studentNameTextField;

    public  String username;
    public  String password;

    public void navigateToStudentPage() throws IOException {
        Stage Stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("StudentDashboard.fxml"));
        Stage2.setScene(new Scene(root));
        Stage2.show();
    }
    public void navigateToCBAPage() throws IOException {
        Stage Stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("StudentDashboard.fxml"));
        Stage2.setScene(new Scene(root));
        Stage2.show();
    }
    public void navigateToAdminPage() throws IOException {
        Stage Stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("StudentDashboard.fxml"));
        Stage2.setScene(new Scene(root));
        Stage2.show();//bhhbb
    }
    public void onStudentLoginClick(ActionEvent event) throws Exception{
        String firstLetterPassword;
        username = studentNameTextField.getText();
        password = passwordTextField.getText();
        System.out.println(password);
        if(username.equals("")){
            usernameErrorText.setText("Enter your username");
        }
        firstLetterPassword = String.valueOf(password.charAt(0));
        if(password.equals("")){
            passwordErrorText.setText("Enter your password");
            return;
        } else if (firstLetterPassword.equals("S123")) {
            usernameErrorText.setText(" ");
            passwordErrorText.setText(" ");
            navigateToStudentPage();
            return;
        }
        passwordErrorText.setText("Incorrect Password");
    }
}
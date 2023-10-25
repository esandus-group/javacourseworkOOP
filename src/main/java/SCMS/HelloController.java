package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class HelloController {
    @FXML
    private Text passwordErrorText;
    @FXML
    private Text usernameErrorText;


    @FXML
    private TextField passwordTextField;

    @FXML
    private Button studentLoginButton;
    @FXML
    private Button advisorLogin;
    @FXML
    private TextField studentNameTextField;

    public  String username;
    public  String password;


    //=====================================================================
    public void navigateToStudentPage() throws IOException {
        Stage Stage2 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("StudentDashboard.fxml"));
        Stage2.setScene(new Scene(root));
        Stage2.show();
    }
    //===============================================================
    public void navigateToAdvisorPage() throws IOException {
        Stage Stage3 = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Club advisor.fxml"));
        Stage3.setScene(new Scene(root));
        Stage3.show();
    }
    //==============================================================================

    //=====================================================================

    //==========================================================================
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
        } else if (password.equals("S123")) {
            usernameErrorText.setText(" ");
            passwordErrorText.setText(" ");
            navigateToStudentPage();
            return;
        }
        passwordErrorText.setText("Incorrect Password");
    }
    public void onAdvisorLoginClick(ActionEvent event) throws Exception{
        navigateToAdvisorPage();
    }



}


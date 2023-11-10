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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MainLoginPageController {
    public ArrayList<String> studentNames = new ArrayList<>();
    public ArrayList<String> studentPassowrd = new ArrayList<>();
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private TextField passwordTextField;

    @FXML
    private Text stdNameErrorText;

    @FXML
    private Text passwordErrorText;

    @FXML
    private TextField studentNameTextField;
    public String username = "Username";
    public String password;
    String fileName;
    Stage stage;

    public void loadStudentData() throws Exception{
        Statement st = connections.createStatement();
        String query = "select * from student";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            studentNames.add(rs.getString(2));
            studentPassowrd.add(rs.getString(5));
        }
        st.close();
        connections.close();
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
    public void Login(ActionEvent event) throws Exception {
        loadStudentData();
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
        for(String student: studentNames){
            System.out.println(student);
        }
        username = studentNameTextField.getText();
        stdNameErrorText.setText(" ");
        if(username.equals("")){
            stdNameErrorText.setText("Please enter your name");
            return false;
        }
        if (!studentNames.contains(username)) {
            stdNameErrorText.setText("Incorrect username");
            return false;
        }
        return true;
    }
    public boolean validatePassword() {
        username = studentNameTextField.getText();
        password = passwordTextField.getText();
        stdNameErrorText.setText(" ");
        passwordErrorText.setText(" ");

        if (username.equals("")) {
            stdNameErrorText.setText("Please enter your name");
            return false;
        }

        if (!studentNames.contains(username)) {
            stdNameErrorText.setText("Incorrect username");
            return false;
        }

        int index = studentNames.indexOf(username);

        if (index >= 0) {
            if (!password.equals(studentPassowrd.get(index))) {
                passwordErrorText.setText("Incorrect password");
                return false;
            }
        } else {
            stdNameErrorText.setText("Username not found");
            return false;
        }

        return true;
    }
    public void onLoginAsClubAdvisorButtonClick(ActionEvent event) throws IOException {
        fileName = "/SCMS/FxmlFiles/Club advisor.fxml";
        stageLoader(event, fileName);
    }



}

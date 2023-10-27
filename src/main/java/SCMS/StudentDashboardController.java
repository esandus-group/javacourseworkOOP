package SCMS;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;

public class StudentDashboardController {

    @FXML
    private Text welcomeText;
    private String fileName;

    @FXML
    private Button club1Button;

    @FXML
    private Button club2Button;

    @FXML
    private Button club3Button;

    String buttonText;

    Stage stage;

    public void setWelcomeText(String name){
        welcomeText.setText(name);
    }

    public void stageLoader(ActionEvent event, String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onRegisterClubButtonClick(ActionEvent event) throws IOException {
        fileName = "RegisterToClubs.fxml";
        stageLoader(event, fileName);
    }
    public void onClub1ButtonClick(ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClubDashboard.fxml"));
        Parent root = loader.load();
        buttonText = club1Button.getText();
        ClubDashboardController CDC = loader.getController();
        CDC.setClubNameText(buttonText);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onClub2ButtonClick(ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClubDashboard.fxml"));
        Parent root = loader.load();
        buttonText = club2Button.getText();
        ClubDashboardController CDC = loader.getController();
        CDC.setClubNameText(buttonText);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void onClub3ButtonClick(ActionEvent event) throws  IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClubDashboard.fxml"));
        Parent root = loader.load();
        buttonText = club3Button.getText();
        ClubDashboardController CDC = loader.getController();
        CDC.setClubNameText(buttonText);
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}

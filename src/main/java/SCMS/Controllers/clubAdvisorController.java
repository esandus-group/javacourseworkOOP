package SCMS.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;


public class clubAdvisorController {

    public int clubIdToDeleteStudent;
    public int studentId;

    public String fileName;
    Stage stage;

    //Database db1 = new Database();
    //=============================================================
    public void stageLoader(ActionEvent event, String fileName) throws IOException { //used to load the new stage
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //---------------------------------------------------------------------
    public void onAddClubClick(ActionEvent event) throws Exception{ // add club
        fileName="/SCMS/FxmlFiles/CreateClub.fxml";      //the fxml path
        stageLoader(event,fileName);                    //call the stage loader method passing the fxml path

    }
    //---------------------------------------------------------------------
    public void onClub1PressClick(ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }

    public void onClub2PressClick(ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }
    public void onClub3PressClick(ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }
    public void onClub4PressClick(ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }

}

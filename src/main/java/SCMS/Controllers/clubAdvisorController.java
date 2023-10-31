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
    public void stageLoader(ActionEvent event, String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    //---------------------------------------------------------------------
    public void onAddClubClick(ActionEvent event) throws Exception{ //nikoyas add club
        fileName="/SCMS/FxmlFiles/CreateClub.fxml";      //open the page
        stageLoader(event,fileName);

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

    public void onRemoveStudentClick(ActionEvent event) throws Exception{
        fileName="/SCMS/FxmlFiles/DeleteStudent.fxml";      //open the page
        stageLoader(event,fileName);

    }

}

package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

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
        fileName="CreateClub.fxml";      //open the page
        stageLoader(event,fileName);

    }
    //---------------------------------------------------------------------
    public void onClub1PressClick(ActionEvent event) throws Exception{
        fileName="PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }

    public void onClub2PressClick(ActionEvent event) throws Exception{
        fileName="PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }
    public void onClub3PressClick(ActionEvent event) throws Exception{
        fileName="PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }
    public void onClub4PressClick(ActionEvent event) throws Exception{
        fileName="PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }

    public void onRemoveStudentClick(ActionEvent event) throws Exception{
        fileName="DeleteStudent.fxml";      //open the page
        stageLoader(event,fileName);

    }



}

package SCMS.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AcceptStudentsController {
    @FXML
    private Button backButtonCDD;

    @FXML
    private Button fillTableButton;
    public void stageLoader(ActionEvent event, String fileName) throws IOException { //STAGE LOADER METHOD
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void backButtonCDD(ActionEvent event) throws IOException {
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }
}

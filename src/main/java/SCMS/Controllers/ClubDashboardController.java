package SCMS.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.io.IOException;

public class ClubDashboardController {
//hello nigga 123
    @FXML
    private Text clubNameText;

    @FXML
    private TableView<?> functionsTable;

    @FXML
    private Button leaveClubButton;
    @FXML





    String name;

    public void setClubNameText(String name){
        clubNameText.setText(name);
    }


}



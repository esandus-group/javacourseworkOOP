package SCMS;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

public class ClubDashboardController {

    @FXML
    private Text clubNameText;

    @FXML
    private TableView<?> functionsTable;

    @FXML
    private Button leaveClubButton;

    String name;

    public void setClubNameText(String name){
        clubNameText.setText(name);
    }

}


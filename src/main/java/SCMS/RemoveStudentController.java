package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RemoveStudentController {

    @FXML
    private TextField deleteStudentClub;

    @FXML
    private TextField deleteStudentId;

    @FXML
    private TextField deleteStudentReason;

    @FXML
    private Button removeStudentButton;

    @FXML
    private Label statusShowLabel;


    public void onRemoveStudentButtonClick(ActionEvent event) throws Exception{

        String studentId = deleteStudentId.getText();

        int clubIdToDeleteStudent = Integer.parseInt(deleteStudentClub.getText());

        String reason = deleteStudentReason.getText();



    }

}

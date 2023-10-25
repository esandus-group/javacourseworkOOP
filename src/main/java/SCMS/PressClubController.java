package SCMS;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;

public class PressClubController {
    @FXML
    private TextField clubIdToDelete;

    public String confirmation;
    public int clubIdDelete;

    @FXML
    private Button deleteClub;

    @FXML
    private TextField confirmText;
    @FXML
    private TextField advisorIdDeleting;

    int advisorIdWhoIsDeleting;
    Database db2 = new Database();

    @FXML
    private TableColumn<?, ?> colAttendance;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colFunction;

    @FXML
    private Button removeStudent;

    @FXML
    private TableView<?> toUsers;

    Stage stage;

    public void stageLoader(ActionEvent event, String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void onRemoveStudentClick(ActionEvent event) throws Exception{
        String fileName="DeleteStudent.fxml";      //open the page
        stageLoader(event,fileName);

    }

    //------------------------------------------------------------------------------

    public void onDeleteClubClick(ActionEvent event) throws Exception {

        confirmation = confirmText.getText();
        clubIdDelete = Integer.parseInt(clubIdToDelete.getText());
        advisorIdWhoIsDeleting = Integer.parseInt(advisorIdDeleting.getText());

        if (confirmation.equals("CONFIRM")) {
            //get the advisor and call the delete method on it
        } else {
            System.out.println("Deletion not confirmed. Club was not deleted.");
        }
    }
    //=========================================================================
}

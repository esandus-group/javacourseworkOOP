package SCMS;

import SCMS.Controllers.PressClubController;
import SCMS.Controllers.RemoveStudentController;
import SCMS.Controllers.ViewStudentsController;
import SCMS.Controllers.createClubController;
import SCMS.Utils.SCMSEnvironment;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    Stage stage;
//    public HelloApplication() throws Exception {
//    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/SCMS/FxmlFiles/MainLoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {

        PressClubController controller3 = new PressClubController();
        RemoveStudentController controller2 = new RemoveStudentController();
        createClubController controller = new createClubController();
        ViewStudentsController controller1 =new ViewStudentsController();
        SCMSEnvironment.getInstance();

        launch();

    }

    public void stageLoader(ActionEvent event, String fileName) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }





}


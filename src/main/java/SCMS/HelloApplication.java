package SCMS;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public HelloApplication() throws Exception {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainLoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {

        PressClubController controller3 = new PressClubController();
        RemoveStudentController controller2 = new RemoveStudentController();
        createClubController controller = new createClubController();
        ViewStudentsController controller1 =new ViewStudentsController();
        launch();

    }





}
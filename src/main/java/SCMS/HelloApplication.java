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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HelloApplication extends Application {
    Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/SCMS/FxmlFiles/MainLoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

//        SQLReader("2023-11");
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

//    public static HashMap<String, String> SQLReader(String yearMonth){
////        yearMonth format => "2023-11"
//        try {
//            String sqlQuery = "SELECT typeOfClubFunction, COUNT(*) AS numberOfEvents FROM Event WHERE DATE_FORMAT(dateTime, '%Y-%m') = ? GROUP BY typeOfClubFunction";
//
//            // Create a PreparedStatement
//            PreparedStatement statement = connections.prepareStatement(sqlQuery);
//            statement.setString(1, yearMonth);
//            ResultSet resultSet = statement.executeQuery();
//
//            // Putting the results in a hashmap for easy acsess
//            HashMap<String, String> eventCount = new HashMap<String, String>();
//            while (resultSet.next()) {
//                System.out.println("here");
//                eventCount.put(resultSet.getString("typeOfClubFunction"), resultSet.getString("numberOfEvents"));
//            }
//            System.out.println(eventCount.get("Meeting"));
//            return eventCount;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    private static Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
}


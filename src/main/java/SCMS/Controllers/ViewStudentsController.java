package SCMS.Controllers;

import SCMS.Objects.Student;
import SCMS.Utils.SCMSEnvironment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class ViewStudentsController {
    @FXML
    private TableView<Student> viewStudentsTable;
    @FXML
    private TextField clubIdStudents;
    @FXML
    private Label idStatusLabel;

    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
    @FXML
    private TableColumn<Student, String> StudentNameCol;
    @FXML
    private TableColumn<Student, String> StudentNameCol1;



    @FXML
    private Button fillTableButton;

    public String idOfClub;

    @FXML
    private TableColumn<Student, String> studentIdCol;
    ArrayList<Student> studentsPresent = new ArrayList<>();

    @FXML
    private Button backButtonCDD;



    //=======================================================
    public void stageLoader(ActionEvent event, String fileName) throws IOException { //STAGE LOADER METHOD
        Parent root = FXMLLoader.load(getClass().getResource(fileName));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        stageLoader(event,fileName);
    }

    //=======================================================
    public ArrayList<Student> getStudentsByClubId(String clubId) {
        ArrayList<Student> studentsList = new ArrayList<>();

        //GETTING THE STUDENT IDS FROM THE TABLE
        // Step 1: Execute a query to get student IDs from Club_Students for the given clubId
        String query1 = "SELECT id FROM Club_Student WHERE clubid = ?";
        try (PreparedStatement statement1 = connections.prepareStatement(query1)) {
            statement1.setString(1, clubId);
            ResultSet resultSet1 = statement1.executeQuery();

            while (resultSet1.next()) {
                String studentId = resultSet1.getString("id");

                //GETTING THE STUDENT OBJECTS USING THOSE IDS FROM STUDENT TABLE
                // Step 2: Execute a query to get student details from the Student table
                String query2 = "SELECT * FROM Student WHERE id = ?";
                try (PreparedStatement statement2 = connections.prepareStatement(query2)) {
                    statement2.setString(1, studentId);
                    ResultSet resultSet2 = statement2.executeQuery();

                    if (resultSet2.next()) {
                        String id = resultSet2.getString("id");
                        String firstName = resultSet2.getString("firstName");
                        String lastName = resultSet2.getString("lastName");
                        String dateOfBirth = resultSet2.getString("dateOfBirth");
                        String password = resultSet2.getString("password");


                        studentsList.add(new Student(id, firstName, lastName,dateOfBirth,password));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return studentsList;
    }
    public boolean checkIfClubExists(String clubId) {
        String query = "SELECT 1 FROM Club WHERE clubId = ?";
        boolean clubExists = false;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, clubId);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                clubExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }

        return clubExists;
    }
    //=======================================================

    public void loadingStudents(){

        StudentNameCol.setCellValueFactory(new PropertyValueFactory<Student,String>("FirstName"));
        StudentNameCol1.setCellValueFactory(new PropertyValueFactory<Student,String>("LastName"));
        studentIdCol.setCellValueFactory(new PropertyValueFactory<Student,String>("id"));

        ObservableList<Student> data = FXCollections.observableArrayList();
        data.addAll(studentsPresent);
        viewStudentsTable.setItems(data);

    }
    //=======================================================

    public void onFillTableButtonClick(ActionEvent event) throws Exception {
        idOfClub = clubIdStudents.getText();

        if (!checkIfClubExists(idOfClub)){
            idStatusLabel.setText("Club not found");
        }

        else {
            studentsPresent = getStudentsByClubId(idOfClub);
            idStatusLabel.setText("");
            loadingStudents();
            fillTableButton.setDisable(true);
        }

    }
    //===================================================================
}

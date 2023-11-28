package SCMS.Controllers;

import SCMS.Objects.Club;
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

public class ViewStudentsController {       //(FULLY DONE BY ESANDU)
    @FXML
    private TableView<Student> viewStudentsTable;
    @FXML
    private Label idStatusLabel;
    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection();
    @FXML
    private TableColumn<Student, String> StudentNameCol;
    @FXML
    private TableColumn<Student, String> StudentNameCol1;
    private String buttonText;
    @FXML
    private Button fillTableButton;

    @FXML
    private TableColumn<Student, String> studentIdCol;
    ArrayList<Student> studentsPresent = new ArrayList<>();
    private String advisorId;
    public Club club;
    @FXML
    private Button backButtonCDD;
    //=======================================================
    public void gettingInformation(Club club,String advisorId){
        this.club=club;
        this.advisorId =advisorId;
    }
    //=======================================================
    public void  backButtonCDD  (ActionEvent event) throws Exception{
        String fileName = "/SCMS/FxmlFiles/PressClub.fxml";      //open the page
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
        Parent root = loader.load();
        buttonText = club.getName();

        PressClubController pcc = loader.getController();
        pcc.setWelcomeText(buttonText,advisorId);

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    //=======================================================
    public ArrayList<Student> getStudentsByClubId(Club club) {
        String clubId = club.getClubId();
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
    public boolean checkIfClubExists(Club club) {
        String query = "SELECT 1 FROM Club WHERE clubId = ?";
        boolean clubExists = false;

        try (PreparedStatement statement = connections.prepareStatement(query)) {
            statement.setString(1, club.getClubId());

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
        if (!checkIfClubExists(club)){
            idStatusLabel.setText("Club not found");
        }

        else {
            studentsPresent = getStudentsByClubId(club);
            idStatusLabel.setText("");
            loadingStudents();
            fillTableButton.setDisable(true);
        }

    }
    //===================================================================
}

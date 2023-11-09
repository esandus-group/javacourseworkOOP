package SCMS.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class RegisterToClubController {
//Load data to the 2 array lists.
    ArrayList<String> studentNames = new ArrayList<>();
    ArrayList<String> studentId = new ArrayList<>();

    ArrayList<String> clubs = new ArrayList<>();

    @FXML
    private TextField stdIdTextField;
    @FXML
    private TextField stdNameTextField;
    @FXML
    private ComboBox clubComboBox;

    String studentFName;
    String stdId;

    public void onRegisterToClubButtonClick() throws Exception{
        String studentId = stdIdTextField.getText();
        String studentName = stdNameTextField.getText();
        String selectedClub = clubComboBox.getValue().toString();

        System.out.println("Here");

        if (isStudentValid(studentId, studentName)) {
            System.out.println("Here2");
            registerStudentToClub(studentId, selectedClub);
        } else {
            // Handle the case where the student is not valid or is already a member
            // Show an error message or prevent registration
        }
    }
    public void setStudentName(String name, String stdId){
        this.studentFName = name;
        this.stdId = stdId;
        setClubsComboBox();
    }


    public void setClubsComboBox() {
        try {
            String url = "jdbc:mysql://localhost:3306/school_club_management?user=root";
            String DBusername = "root";
            String DBpassowrd = "esandu12345";
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, DBusername, DBpassowrd);
            Statement st = con.createStatement();
            System.out.println(stdId);
            String joinedClubsQuery = "select * from club_student where id !='"+stdId+"'";
            ResultSet joinedClubs = st.executeQuery(joinedClubsQuery);
            while(joinedClubs.next()){
                clubs.add(joinedClubs.getString("clubId"));
            }
            for(String club: clubs){
                System.out.println(club);
            }
            clubComboBox.getItems().addAll(clubs);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public boolean isStudentValid(String stdId, String stdName){
        System.out.println(stdId + stdName);
        for(String name:studentNames){
            System.out.println(name);
        }
        int index = studentNames.indexOf(stdName);
        System.out.println(index);
        if(stdName.equals("")){
            return false;
        }
        if (!studentNames.contains(stdName)) {
            return false;
        }

        if (index >= 0) {
            if (!stdId.equals(studentId.get(index))) {
                return false;
            }
        }

        return true;
    }
    public void registerStudentToClub(String clubId, String stdId) throws Exception{
        String url = "jdbc:mysql://localhost:3306/school_club_management?user=root";
        String DBusername = "root";
        String DBpassowrd = "esandu12345";
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection(url, DBusername, DBpassowrd);
        Statement st = con.createStatement();
        String query = "INSERT INTO  culb_student VALUES (clubId, stdId);";
        st.executeQuery(query);
        System.out.println("Done");
    }


}

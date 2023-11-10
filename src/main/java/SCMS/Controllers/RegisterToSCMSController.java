package SCMS.Controllers;

import SCMS.Utils.SCMSEnvironment;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegisterToSCMSController {
    ArrayList<String> allStudentId = new ArrayList<>();

    private Connection connections = SCMSEnvironment.getInstance().makeSqlDBConnection(); //GETtING THE CONNECTION OF THE DB

    @FXML
    private Button requestToJoinButton;

    @FXML
    private DatePicker stdDOBDatePicker;

    @FXML
    private TextField stdFirstNameTextField;

    @FXML
    private TextField stdIdTextField;

    @FXML
    private TextField stdLastNameTextField;

    @FXML
    private TextField stdPasswordTextField;
    String stdId;
    String stdFName;
    String stdLName;
    LocalDateTime stdDOB;
    String stdPassword;

    @FXML
    void onRequestToJoinButtonClick(ActionEvent event) throws Exception {
        stdId = stdIdTextField.getText();
        stdFName = stdFirstNameTextField.getText();
        stdLName = stdLastNameTextField.getText();
        stdDOB = stdDOBDatePicker.getValue().atStartOfDay();
        stdPassword = stdPasswordTextField.getText();

        if(isStudentIdValid(stdId)){

        }
    }
    public boolean isStudentIdValid(String studentId) throws Exception{
        Statement st = connections.createStatement();
        String getstdIdQuery = "select * from student where id = '"+stdId+"'";
        ResultSet rs = st.executeQuery(getstdIdQuery);
        while(rs.next()){
            allStudentId.add(rs.getString(1));
        }

        if (studentId != null){
            return allStudentId.contains(studentId);
        }

        return false;

    }

}

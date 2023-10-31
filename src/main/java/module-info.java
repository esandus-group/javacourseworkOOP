module com.example.javacw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires io.github.cdimascio.dotenv.java;


    opens SCMS to javafx.fxml;
    exports SCMS;
    exports SCMS.Controllers;
    opens SCMS.Controllers to javafx.fxml;
    exports SCMS.Objects;
    opens SCMS.Objects to javafx.fxml;
}
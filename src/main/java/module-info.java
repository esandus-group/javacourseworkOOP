module com.example.javacw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens SCMS to javafx.fxml;
    exports SCMS;
}
module com.example.javacw {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens SCMS to javafx.fxml;
    exports SCMS;
}
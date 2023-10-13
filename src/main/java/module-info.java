module com.example.javacw {
    requires javafx.controls;
    requires javafx.fxml;


    opens SCMS to javafx.fxml;
    exports SCMS;
}
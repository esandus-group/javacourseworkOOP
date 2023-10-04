module com.example.javacw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javacw to javafx.fxml;
    exports com.example.javacw;
}
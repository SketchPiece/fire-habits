module org.example.firehabits {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.firehabits to javafx.fxml;
    exports org.example.firehabits;
}
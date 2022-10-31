module com.mrdmi.snake {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mrdmi.snake to javafx.fxml;
    exports com.mrdmi.snake;
}
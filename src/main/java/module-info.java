module com.example.livestock_inventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.livestock_inventory to javafx.fxml;
    opens controllers to javafx.fxml;
    opens models.interfaces to javafx.fxml;
    exports com.example.livestock_inventory;
    exports controllers;
    exports models.interfaces;
}
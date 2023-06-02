module com.example.livestock_inventory {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io;
    requires kernel;
    requires layout;


    opens com.example.livestock_inventory to javafx.fxml;
    opens controllers to javafx.fxml;
    opens models.interfaces to javafx.fxml;
    exports com.example.livestock_inventory;
    exports controllers;
    exports models.interfaces;
    exports controllers.inventory;
    opens controllers.inventory to javafx.fxml;
    exports controllers.components;
    opens controllers.components to javafx.fxml;
    exports controllers.owners;
    opens controllers.owners to javafx.fxml;
    exports controllers.tasks;
    opens controllers.tasks to javafx.fxml;
    exports controllers.finances;
    opens controllers.finances to javafx.fxml;
    exports controllers.veterinaryAssistance;
    opens controllers.veterinaryAssistance to javafx.fxml;
}
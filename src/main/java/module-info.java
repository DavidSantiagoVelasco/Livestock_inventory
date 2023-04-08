module com.example.livestock_inventory {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.livestock_inventory to javafx.fxml;
    exports com.example.livestock_inventory;
}
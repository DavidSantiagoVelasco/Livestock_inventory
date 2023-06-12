package controllers.inventory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InventoryController {

    @FXML
    void buyAnimal() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/buyAnimalModule.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();
            stage.setTitle("Agregar animal");
            stage.setScene(inventoryScene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void showInventory() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/showInventory.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();
            stage.setTitle("Inventario");
            stage.setScene(inventoryScene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}

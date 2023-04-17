package controllers.inventory;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {


    @FXML
    private Button btnBuyAnimals;

    @FXML
    private Button btnDeleteAnimals;

    @FXML
    private Button btnModifyInventory;

    @FXML
    private Button btnSellAnimals;

    @FXML
    private Button btnShowInventory;

    @FXML
    void buyAnimals(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/buyAnimalModule.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void deleteAnimals(ActionEvent event) {

    }

    @FXML
    void sellAnimals(ActionEvent event) {

    }

    @FXML
    void showInventory(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/showInventory.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void showOptionsModifyInventory(ActionEvent event) {
        this.btnBuyAnimals.setVisible(true);
        this.btnSellAnimals.setVisible(true);
        this.btnDeleteAnimals.setVisible(true);
        this.btnModifyInventory.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

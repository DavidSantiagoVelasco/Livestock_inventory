package controllers.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    private Button btnFinance;

    @FXML
    private Button btnInventory;

    @FXML
    private Button btnTasks;

    @FXML
    private Button btnVeterinaryAssistance;

    @FXML
    private void openFinance() {

    }

    @FXML
    private void openInventory() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/inventory.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

            Stage currentStage = (Stage) this.btnFinance.getScene().getWindow();
            currentStage.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openTasks() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/tasks/tasks.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

            Stage currentStage = (Stage) this.btnFinance.getScene().getWindow();
            currentStage.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void openVeterinaryAssistance() {

    }

    @FXML
    private void openOwners() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/owners/owners.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

            Stage currentStage = (Stage) this.btnFinance.getScene().getWindow();
            currentStage.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

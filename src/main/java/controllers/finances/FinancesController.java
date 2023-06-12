package controllers.finances;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FinancesController {

    @FXML
    private Button btnAddFinances;

    public void showFinances() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/finances/showFinances.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();
            stage.setTitle("Finanzas");
            stage.setScene(inventoryScene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    public void addFinances() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/finances/createFinance.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

            Stage currentStage = (Stage) this.btnAddFinances.getScene().getWindow();
            currentStage.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}

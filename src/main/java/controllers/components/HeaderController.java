package controllers.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HeaderController {

    @FXML
    private Label lblTitle;

    @FXML
    private void openDashboard(MouseEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/dashboard.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

            Stage currentStage = (Stage) this.lblTitle.getScene().getWindow();
            currentStage.close();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}

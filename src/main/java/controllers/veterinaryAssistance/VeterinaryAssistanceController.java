package controllers.veterinaryAssistance;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class VeterinaryAssistanceController {

    @FXML
    private void showAssistance() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/veterinaryAssistance/showVeterinaryAssistance.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();
            stage.setTitle("Asistencias veterinarias");
            stage.setScene(inventoryScene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addCompletedAssistance() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/veterinaryAssistance/createCompletedVeterinaryAssistance.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();
            stage.setTitle("Agregar asistencia veterinaria");
            stage.setScene(inventoryScene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void addScheduledAssistance() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/veterinaryAssistance/createAssignedVeterinaryAssistance.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();
            stage.setTitle("Agregar asistencia veterinaria");
            stage.setScene(inventoryScene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }
}

package controllers.tasks;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TasksController {
    @FXML
    private void showTasks() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/tasks/showTasks.fxml"));
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
    private void addTask() {
    }
}

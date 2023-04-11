package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import models.TasksModel;
import models.interfaces.Task;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private TasksModel tasksModel;

    @FXML
    private BorderPane dashboardPane;
    @FXML
    private Button btnInventory, btnVeterinaryAssistance, btnFinance, btnTasks;
    @FXML
    private Label lblTasks;
    @FXML
    private TableView<Task> tblTasks;
    @FXML
    private TableColumn<Task, String> colTaskName;
    @FXML
    private TableColumn<Task, String>  colTaskDescription;
    @FXML
    private TableColumn<Task, Date>  colTaskCreationDate;
    @FXML
    private TableColumn<Task, Date> colTaskAssignedDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colTaskName.setCellValueFactory(new PropertyValueFactory("name"));
        this.colTaskDescription.setCellValueFactory(new PropertyValueFactory("description"));
        this.colTaskCreationDate.setCellValueFactory(new PropertyValueFactory("creationDate"));
        this.colTaskAssignedDate.setCellValueFactory(new PropertyValueFactory("assignedDate"));

        this.tasksModel = new TasksModel();
        ObservableList<Task> tasks = tasksModel.getTasksMonth();
        if(tasks == null){
            return;
        }
        if(tasks.size() == 0){
            return;
        }
        this.lblTasks.setText("Recordatorios del mes");
        this.tblTasks.setVisible(true);
        this.tblTasks.setItems(tasks);
    }

    @FXML
    private void openInventory(ActionEvent event) {
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
    private void openVeterinaryAssistance(ActionEvent event) {
    }

    @FXML
    private void openFinance(ActionEvent event) {
    }

    @FXML
    private void openTasks(ActionEvent event) {
    }
}

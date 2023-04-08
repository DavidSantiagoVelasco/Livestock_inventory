package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.TasksModel;
import models.interfaces.Task;

import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private TasksModel tasksModel;

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
    private TableColumn<Task, Timestamp>  colTaskCreationDate;
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
        this.tblTasks.setVisible(true);
        this.tblTasks.setItems(tasks);
    }

    @FXML
    private void openInventory(ActionEvent event) {
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

package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Model;
import models.interfaces.Task;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    private final Model model = new Model();

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
        this.colTaskName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colTaskDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colTaskCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        this.colTaskAssignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));

        ObservableList<Task> tasks = model.getActiveTasksMonth();
        if(tasks == null){
            return;
        }
        if(tasks.size() == 0){
            return;
        }
        this.lblTasks.setText("Recordatorios del mes");
        this.tblTasks.setVisible(true);
        this.tblTasks.setItems(tasks);

        tblTasks.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblTasks.getSelectionModel().getSelectedItem() != null) {
                TablePosition pos = tblTasks.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn col = pos.getTableColumn();
                Task task = tblTasks.getItems().get(row);
                if (col == colTaskDescription) {
                    String observations = task.getDescription();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Descripción del recordatorio");
                    alert.setContentText(observations);
                    alert.showAndWait();
                }
            }
        });
    }
}

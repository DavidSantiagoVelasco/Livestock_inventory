package controllers.tasks;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import models.Model;
import models.interfaces.Task;

import java.sql.Date;
import java.time.ZoneId;

public class CreateTaskController {

    private final Model model = new Model();

    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtDescription;
    @FXML
    private DatePicker dpAssignedDate;

    public void createTask() {
        if(txtName.getLength() == 0 || txtDescription.getLength() == 0 || dpAssignedDate.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        Task task = model.createTask(txtName.getText(), txtDescription.getText(),
                new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        if(task == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setHeaderText("Ocurrió un error. Intente más tarde");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Éxito");
        alert.setHeaderText("Éxito creando el recordatorio");
        alert.showAndWait();
        clearValues();
    }

    private void clearValues(){
        txtName.setText("");
        txtDescription.setText("");
        dpAssignedDate.setValue(null);
    }
}

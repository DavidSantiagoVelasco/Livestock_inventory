package controllers.tasks;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Task;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

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
        LocalDate currentDate = LocalDate.now();
        if(!currentDate.isBefore(dpAssignedDate.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Fecha asignada incongruente");
            alert.setHeaderText("La fecha asignada no puede ser menor que la fecha actual");
            alert.showAndWait();
            dpAssignedDate.setValue(null);
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmación");
        confirmation.setHeaderText("¿Está seguro que desea crear el recordatorio?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isEmpty()){
            return;
        }
        if (result.get() != ButtonType.OK) {
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

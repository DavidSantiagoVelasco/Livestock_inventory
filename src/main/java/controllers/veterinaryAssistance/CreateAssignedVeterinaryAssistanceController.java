package controllers.veterinaryAssistance;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Task;
import models.interfaces.VeterinaryAssistance;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class CreateAssignedVeterinaryAssistanceController {

    private final Model model = new Model();

    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dpAssignedDate;
    @FXML
    private CheckBox cbCreateTask;
    @FXML
    private TextArea txtDescription;

    @FXML
    private void createVeterinaryAssistance() {
        if (txtName.getLength() == 0 || dpAssignedDate.getValue() == null) {
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
        confirmation.setHeaderText("¿Está seguro que desea crear la asistencia veterinaria?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.get() == ButtonType.OK) {
            VeterinaryAssistance veterinaryAssistance = model.createAssignedVeterinaryAssistance(txtName.getText(),
                    new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                    txtDescription.getText());
            if (veterinaryAssistance == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo crear la asistencia veterinaria");
                alert.showAndWait();
                return;
            }
            String taskString = "";
            if (cbCreateTask.isSelected()) {
                Task task = model.createTask(txtName.getText(), "Asistencia veterinaria: " + txtDescription.getText(),
                        new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                        veterinaryAssistance.getId());
                if (task == null) {
                    taskString = "\nNo se pudo crear el recordatorio";
                } else {
                    taskString = "\nÉxito creando el recordatorio";
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Éxito");
            alert.setHeaderText("Éxito creando la asistencia veterinaria" + taskString);
            alert.showAndWait();
            restartValues();
        }
    }

    private void restartValues(){
        txtName.setText("");
        txtDescription.setText("");
        cbCreateTask.setSelected(false);
        dpAssignedDate.setValue(null);
    }
}

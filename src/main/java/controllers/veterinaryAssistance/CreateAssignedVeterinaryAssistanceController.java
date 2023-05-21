package controllers.veterinaryAssistance;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Task;
import models.interfaces.VeterinaryAssistance;

import java.sql.Date;
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
                        new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
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

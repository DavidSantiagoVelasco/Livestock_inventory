package controllers.veterinaryAssistance;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Task;
import models.interfaces.VeterinaryAssistance;

import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class CreateCompletedVeterinaryAssistanceController implements Initializable {

    private final Model model = new Model();

    @FXML
    private TextField txtName;
    @FXML
    private TextArea txtDescription;
    @FXML
    private DatePicker dpCompletedDate;
    @FXML
    private TextField txtCost;
    @FXML
    private CheckBox cbRepeatAssistance;
    @FXML
    private Label lblAssignedDate;
    @FXML
    private DatePicker dpAssignedDate;
    @FXML
    private CheckBox cbCreateTask;

    private boolean showInformationRepeatAssistanceExpense = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TextFormatter<String> textFormatterAmount = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        txtCost.setTextFormatter(textFormatterAmount);
    }

    @FXML
    private void createVeterinaryAssistance() {
        if (txtName.getLength() == 0 || dpCompletedDate.getValue() == null || (cbRepeatAssistance.isSelected() && dpAssignedDate.getValue() == null)) {
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
            VeterinaryAssistance veterinaryAssistance = model.createCompletedVeterinaryAssistance(txtName.getText(),
                    new Date(dpCompletedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                    Double.parseDouble(txtCost.getText()), txtDescription.getText(),
                    new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
            if (veterinaryAssistance == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo crear la asistencia veterinaria");
                alert.showAndWait();
                return;
            }
            if (txtCost.getLength() > 0 && !txtCost.getText().equals("0")) {
                model.addExpense(Double.parseDouble(txtCost.getText()),
                        new Date(dpCompletedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                        "Asistencia veterinaria: " + txtDescription.getText());
            }
            if (cbRepeatAssistance.isSelected()) {
                VeterinaryAssistance repeatVeterinaryAssistance = model.createAssignedVeterinaryAssistance(txtName.getText(),
                        new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                        "Repetir: " + txtDescription.getText());
                Task task;
                String taskString = "";
                if (cbCreateTask.isSelected()) {
                    task = model.createTask(txtName.getText(), "Asistencia veterinaria: " + txtDescription.getText(),
                            new Date(dpAssignedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                            veterinaryAssistance.getId());
                    if (task == null) {
                        taskString = "\nNo se pudo crear el recordatorio";
                    } else {
                        taskString = "\nÉxito creando el recordatorio";
                    }
                }
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Éxito creando la asistencia veterinaria");
                if (repeatVeterinaryAssistance == null) {
                    alert.setContentText("No se pudo crear la próxima asistencia veterinaria" + taskString);
                } else {
                    alert.setContentText("Éxito creando la próxima asistencia veterinaria" + taskString);
                }
                alert.showAndWait();
                restartValues();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Éxito");
            alert.setHeaderText("Éxito creando la asistencia veterinaria");
            alert.showAndWait();
            restartValues();
        }
    }

    @FXML
    private void repeatAssistance() {
        if (cbRepeatAssistance.isSelected()) {
            if (txtCost.getLength() > 0 && showInformationRepeatAssistanceExpense) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Crear próxima asistencia veterinaria");
                alert.setHeaderText("Información crear egreso");
                alert.setContentText("Se creará una próxima asistencia veterinaria pero no se podrá agregar el " +
                        "egreso con el valor del costo para la próxima asistencia veterinaria.\nPodrá agregar el " +
                        "egreso cuando finalice la próxima asistencia veterinaria marcándola como completada en " +
                        "el módulo Ver Asistencias");
                alert.showAndWait();
                showInformationRepeatAssistanceExpense = false;
            }
            lblAssignedDate.setVisible(true);
            dpAssignedDate.setVisible(true);
            cbCreateTask.setVisible(true);
        } else {
            lblAssignedDate.setVisible(false);
            dpAssignedDate.setVisible(false);
            dpAssignedDate.setValue(null);
            cbCreateTask.setVisible(false);
            cbCreateTask.setSelected(false);
        }
    }

    private void restartValues() {
        txtName.setText("");
        dpCompletedDate.setValue(null);
        txtCost.setText("");
        cbRepeatAssistance.setSelected(false);
        txtDescription.setText("");
        repeatAssistance();
    }
}

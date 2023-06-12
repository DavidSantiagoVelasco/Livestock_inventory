package controllers.veterinaryAssistance;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.Task;
import models.interfaces.VeterinaryAssistance;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class CompleteVeterinaryAssistanceController implements Initializable {

    private final Model model = new Model();
    private final VeterinaryAssistance veterinaryAssistance;
    private final ShowVeterinaryAssistanceController showVeterinaryAssistanceController;
    private final int descriptionLength;
    private boolean showInformationRepeatAssistanceExpense = true;

    @FXML
    private TextField txtName;
    @FXML
    private DatePicker dpCompletedDate;
    @FXML
    private TextField txtCost;
    @FXML
    private CheckBox cbRepeatAssistance;
    @FXML
    private TextArea txtDescription;
    @FXML
    private Label lblAssignedDate;
    @FXML
    private DatePicker dpAssignedDate;
    @FXML
    private CheckBox cbCreateTask;
    @FXML
    private Button btnCreateVeterinaryAssistance;

    public CompleteVeterinaryAssistanceController(VeterinaryAssistance veterinaryAssistance,
                                                  ShowVeterinaryAssistanceController showVeterinaryAssistanceController){
        this.veterinaryAssistance = veterinaryAssistance;
        this.showVeterinaryAssistanceController = showVeterinaryAssistanceController;

        this.descriptionLength = veterinaryAssistance.getDescription().length();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtName.setText(veterinaryAssistance.getName());
        txtDescription.setText(veterinaryAssistance.getDescription());

        txtDescription.setOnKeyTyped(keyEvent -> {
            String newText = txtDescription.getText() + keyEvent.getCharacter();
            if(!newText.substring(0, descriptionLength).equals(veterinaryAssistance.getDescription()) || newText.length() < descriptionLength){
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                txtDescription.setText(veterinaryAssistance.getDescription());
                alertError.setTitle("Error");
                alertError.setHeaderText("La antigua descripción no puede ser modificada.");
                alertError.showAndWait();
            }
        });

        btnCreateVeterinaryAssistance.setOnAction(event -> completeVeterinaryAssistance());
        cbRepeatAssistance.setOnAction(event -> repeatAssistance());
    }

    private void completeVeterinaryAssistance() {
        if(dpCompletedDate.getValue() == null || (cbRepeatAssistance.isSelected() && dpAssignedDate.getValue() == null)){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        if(cbRepeatAssistance.isSelected()){
            if(!checkDateConsistency(dpCompletedDate.getValue(), dpAssignedDate.getValue())){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
                alert.setTitle("Inconsistencia en las fechas");
                alert.setHeaderText("La fecha de realización no puede ser mayor a la fecha asignada");
                alert.showAndWait();
                dpAssignedDate.setValue(null);
                return;
            }
        }
        Alert alertConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirm.setTitle("Confirmación");
        alertConfirm.setHeaderText("¿Está seguro de querer completar la asistencia veterinaria?");

        Optional<ButtonType> result = alertConfirm.showAndWait();
        if(result.isEmpty()){
            return;
        }
        if (result.get() != ButtonType.OK) {
            return;
        }
        boolean modifyDescription = false;
        if(!veterinaryAssistance.getDescription().equals(txtDescription.getText())){
            modifyDescription = true;
            veterinaryAssistance.setDescription(txtDescription.getText());
        }
        boolean modifyCost = false;
        if (txtCost.getLength() > 0 && !txtCost.getText().equals("0")) {
            modifyCost = true;
            veterinaryAssistance.setCost(Double.parseDouble(txtCost.getText()));
            model.addExpense(Double.parseDouble(txtCost.getText()),
                    new Date(dpCompletedDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()),
                    "Asistencia veterinaria: " + txtDescription.getText());
        }
        veterinaryAssistance.setDescription(txtDescription.getText());
        boolean response = model.completeVeterinaryAssistance(veterinaryAssistance, modifyDescription, modifyCost);
        if(!response){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("Ocurrió un error. Por favor intente más tarde");
            alertError.showAndWait();
            return;
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
            alert.setHeaderText("Éxito completando la asistencia veterinaria");
            if (repeatVeterinaryAssistance == null) {
                alert.setContentText("No se pudo crear la próxima asistencia veterinaria" + taskString);
            } else {
                alert.setContentText("Éxito creando la próxima asistencia veterinaria" + taskString);
            }
            alert.showAndWait();
            completeVeterinaryAssistanceFinished();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Éxito");
        alert.setHeaderText("Éxito completando la asistencia veterinaria");
        alert.showAndWait();
        completeVeterinaryAssistanceFinished();
    }

    private void repeatAssistance() {
        if (cbRepeatAssistance.isSelected()) {
            if (txtCost.getLength() > 0 && !txtCost.getText().equals("0") && showInformationRepeatAssistanceExpense) {
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

    private void completeVeterinaryAssistanceFinished() {
        Stage currentStage = (Stage) txtDescription.getScene().getWindow();
        showVeterinaryAssistanceController.getVeterinaryAssistance();
        currentStage.close();
    }

    private boolean checkDateConsistency(LocalDate fromDate, LocalDate toDate) {
        Date from = Date.valueOf(fromDate);
        Date to = Date.valueOf(toDate);
        return !to.before(from);
    }
}

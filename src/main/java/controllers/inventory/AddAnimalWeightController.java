package controllers.inventory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.Animal;

import java.net.URL;
import java.sql.Date;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class AddAnimalWeightController implements Initializable {

    private final Animal animal;
    private final Model model = new Model();
    private int response = 0;

    @FXML
    private Label txtHeader;
    @FXML
    private TextField txtWeight;
    @FXML
    private DatePicker dpDate;
    @FXML
    private Button btnAddWeight;
    @FXML
    private Button btnCancel;

    public AddAnimalWeightController(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtHeader.setText("Agregar peso animal No. " + animal.getNumber());

        TextFormatter<String> textFormatterWeight = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        btnAddWeight.setOnAction(event -> addWeight());
        btnCancel.setOnAction(event -> cancel());

        txtWeight.setTextFormatter(textFormatterWeight);
    }

    @FXML
    private void addWeight() {
        if (txtWeight.getText().length() == 0 || dpDate.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        boolean response = model.addAnimalWeight(animal.getId(), animal.getNumber(), Double.parseDouble(txtWeight.getText()),
                new Date(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        if (response) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Éxito agregando el peso");
            alert.showAndWait();
            this.response = 1;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Ocurrió un error. Intente más tarde");
            alert.showAndWait();
            this.response = -1;
        }
        Stage stage = (Stage) txtWeight.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) txtWeight.getScene().getWindow();
        stage.close();
    }

    public int getResponse() {
        return response;
    }
}

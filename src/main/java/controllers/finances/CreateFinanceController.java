package controllers.finances;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Finance;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CreateFinanceController implements Initializable {

    private final Model model = new Model();

    @FXML
    private TextField txtAmount;
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox<String> cbFinanceType;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbFinanceType.setItems(FXCollections.observableArrayList("Ingreso", "Egreso"));

        TextFormatter<String> textFormatterAmount = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        txtAmount.setTextFormatter(textFormatterAmount);
    }

    @FXML
    private void createFinance() {
        if(cbFinanceType.getValue() == null || txtAmount.getLength() == 0 || txtDescription.getLength() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        try {
            LocalDate currentDate = LocalDate.now();
            Date sqlDate = Date.valueOf(currentDate);
            Finance finance = null;
            if(cbFinanceType.getValue().equals("Ingreso")){
                finance = model.addIncome(Double.parseDouble(txtAmount.getText()), sqlDate, txtDescription.getText());
            } else if (cbFinanceType.getValue().equals("Egreso")) {
                finance = model.addExpense(Double.parseDouble(txtAmount.getText()), sqlDate, txtDescription.getText());
            }
            if(finance == null){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo crear el " + cbFinanceType.getValue());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Éxito");
                alert.setHeaderText("Éxito creando el " + cbFinanceType.getValue());
                alert.showAndWait();
                restartValues();
            }
        }catch (Error e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo agregar el " + cbFinanceType.getValue());
            alert.showAndWait();
        }
    }

    private void restartValues() {
        cbFinanceType.setValue(null);
        txtAmount.setText("");
        txtDescription.setText("");
    }
}

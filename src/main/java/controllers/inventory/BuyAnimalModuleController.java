package controllers.inventory;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class BuyAnimalModuleController implements Initializable {

    private String sex = "";


    @FXML
    private Button btnAddAnimal;

    @FXML
    private ComboBox cbSex;

    @FXML
    private TextField txtAgeMonths;

    @FXML
    private TextField txtColor;

    @FXML
    private TextField txtIronBrand;

    @FXML
    private TextField txtNumber;

    @FXML
    private TextArea txtObservations;

    @FXML
    private TextField txtOwner;

    @FXML
    private TextField txtWeight;

    @FXML
    private void addAnimal(ActionEvent event) {
        if(txtNumber.getLength() == 0 || txtOwner.getLength() == 0 || txtAgeMonths.getLength() == 0 || txtColor.getLength() == 0 || txtWeight.getLength() == 0 || txtIronBrand.getLength() == 0 || this.sex.length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
    }

    @FXML
    private void selectSex(ActionEvent event)  {
        this.sex = cbSex.getSelectionModel().getSelectedItem().toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbSex.setItems(FXCollections.observableArrayList("Macho", "Hembra"));

        TextFormatter<String> textFormatterAge = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        TextFormatter<String> textFormatterWeight = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        this.txtWeight.setTextFormatter(textFormatterWeight);
        this.txtAgeMonths.setTextFormatter(textFormatterAge);
    }

    @FXML
    private void checkInputNumbers(KeyEvent keyEvent) {
        if(!keyEvent.getCharacter().matches("[0-9]")){
            keyEvent.consume();
        }
    }
}

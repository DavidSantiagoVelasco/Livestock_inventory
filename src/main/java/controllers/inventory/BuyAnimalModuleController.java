package controllers.inventory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Animal;
import models.interfaces.Owner;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;


public class BuyAnimalModuleController implements Initializable {

    private Model model;
    private String sex = "";

    @FXML
    private ComboBox cbSex;
    @FXML
    private ComboBox cbOwners;
    @FXML
    private TextField txtAgeMonths;
    @FXML
    private TextField txtColor;
    @FXML
    private TextField txtIronBrand;
    @FXML
    private TextField txtNumber;
    @FXML
    private TextField txtPurchasePrice;
    @FXML
    private TextArea txtObservations;
    @FXML
    private TextField txtWeight;
    @FXML
    private DatePicker dpPurchaseDate;

    @FXML
    private void addAnimal() {
        if(cbOwners.getValue() == null || dpPurchaseDate.getValue() == null || txtNumber.getLength() == 0 ||
                txtAgeMonths.getLength() == 0 || txtColor.getLength() == 0 || txtPurchasePrice.getLength() == 0 ||
                txtWeight.getLength() == 0 || txtIronBrand.getLength() == 0 || this.sex.length() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        LocalDate currentDate = LocalDate.now();
        if(currentDate.isBefore(dpPurchaseDate.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Fecha de compra incongruente");
            alert.setHeaderText("No puede agregar un animal con una fecha mayor a la actual");
            alert.showAndWait();
            return;
        }
        Animal animalExists = model.getActiveAnimalByNumber(txtNumber.getText());

        if(animalExists != null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Existe animal");
            alert.setHeaderText("Ya existe un animal activo con el mismo número");
            alert.showAndWait();
            return;
        }
        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setTitle("Confirmación");
        alertConfirmation.setHeaderText("¿Está seguro de agregar el animal?");

        Optional<ButtonType> result = alertConfirmation.showAndWait();
        if (result.get() != ButtonType.OK){
            return;
        }
        String selectedOwner = (String) cbOwners.getSelectionModel().getSelectedItem();
        int idSelectedOwner = model.getOwnerIdFromOwnerInformation(selectedOwner);

        Animal animal = model.createAnimal(idSelectedOwner, txtNumber.getText(), Integer.parseInt(txtAgeMonths.getText()),
                txtColor.getText(), Double.parseDouble(txtWeight.getText()), txtIronBrand.getText(), sex.charAt(0),
                Double.parseDouble(txtPurchasePrice.getText()), txtObservations.getText(),
                new Date(dpPurchaseDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        if(animal == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo agregar el animal");
            alert.showAndWait();
            return;
        }

        Owner owner = model.getOwnerById(animal.getIdOwner());

        if(owner.getPercentage() == 0){
            String description = "Compra del animal con número: " + animal.getNumber() + " y id: " + animal.getId();
            model.addExpense(animal.getPurchasePrice(), animal.getPurchaseDate(), description);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Éxito");
        alert.setHeaderText("Éxito agregando el nuevo animal para el dueño");
        alert.showAndWait();
        restartValues();
    }

    @FXML
    private void selectSex()  {
        this.sex = cbSex.getSelectionModel().getSelectedItem().toString();
    }

    @FXML
    private void selectOwner() {
        String ironBrand = model.getOwnerIronBrandFromOwnerInformation(cbOwners.getSelectionModel().getSelectedItem().toString());
        txtIronBrand.setText(ironBrand);
    }

    private void restartValues(){
        txtNumber.setText("");
        txtAgeMonths.setText("");
        txtColor.setText("");
        txtWeight.setText("");
        txtIronBrand.setText("");
        txtPurchasePrice.setText("");
        dpPurchaseDate.setValue(null);
        txtObservations.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new Model();
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

        TextFormatter<String> textFormatterPrice = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        txtWeight.setTextFormatter(textFormatterWeight);
        txtAgeMonths.setTextFormatter(textFormatterAge);
        txtPurchasePrice.setTextFormatter(textFormatterPrice);
        model.setOwnersInformation(cbOwners);
    }
}

package controllers.inventory;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.Animal;
import models.interfaces.Owner;

import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellAnimalController implements Initializable {

    private Model model;
    private final Animal animal;
    private final ShowInventoryController showInventoryController;

    @FXML
    private TextField txtNumber;
    @FXML
    private ComboBox<String> cbOwners;
    @FXML
    private TextField txtColor;
    @FXML
    private TextField txtPurchaseWeight;
    @FXML
    private TextField txtSaleWeight;
    @FXML
    private TextField txtIronBrand;
    @FXML
    private TextField txtSalePrice;
    @FXML
    private DatePicker dpSaleDate;
    @FXML
    private TextArea txtObservations;
    @FXML
    private Button btnSellAnimal;
    @FXML
    private TextField txtSex;
    @FXML
    private TextField txtPurchasePrice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new Model();
        Owner owner = model.getOwnerById(animal.getIdOwner());
        animal.setOwnerInformation(owner);
        cbOwners.setItems(FXCollections.observableArrayList(animal.getOwnerInformation()));
        cbOwners.getSelectionModel().selectFirst();
        txtNumber.setText(animal.getNumber());
        txtColor.setText(animal.getColor());
        txtSex.setText(animal.getSex() == 'M' ? "Macho" : "Hembra" );
        txtIronBrand.setText(animal.getIronBrand());
        txtPurchaseWeight.setText(animal.getPurchaseWeight()+"");
        txtPurchasePrice.setText(animal.getPurchasePrice()+"");

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

        txtSaleWeight.setTextFormatter(textFormatterWeight);
        txtSalePrice.setTextFormatter(textFormatterPrice);

        btnSellAnimal.setOnAction(e -> sellAnimal());
    }

    public SellAnimalController(Animal animal, ShowInventoryController showInventoryController) {
        this.animal = animal;
        this.showInventoryController = showInventoryController;
    }

    public void sellAnimal() {
        if(dpSaleDate.getValue() == null || txtSalePrice.getLength() == 0 || txtSaleWeight.getLength() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro que desea liquidar el animal?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty()){
            return;
        }
        if (result.get() == ButtonType.OK){
            int id = model.getOwnerIdFromOwnerInformation(cbOwners.getValue());
            Owner owner = model.getOwnerById(id);
            double income;
            if(owner.getPercentage() == 0){
                income = Double.parseDouble(txtSalePrice.getText());
            } else {
                double weightGain = Double.parseDouble(txtSaleWeight.getText()) - animal.getPurchaseWeight();
                double kgPrice = Double.parseDouble(txtSalePrice.getText())/Double.parseDouble(txtSaleWeight.getText());
                income = weightGain * kgPrice;
            }
            double ownerIncome = (income * owner.getPercentage()) / 100;
            income = income - ownerIncome;
            DecimalFormat formatter = new DecimalFormat("$#,###.###");
            Alert alertIncome = new Alert(Alert.AlertType.CONFIRMATION, "Liquidación");
            if(owner.getPercentage() != 0){
                alertIncome.setTitle("¿Confirma liquidación?");
                alertIncome.setHeaderText("Debe entregar al dueño "+formatter.format(ownerIncome));
                alertIncome.setContentText("A usted le corresponde "+formatter.format(income));
            } else {
                double ganances = income - animal.getPurchasePrice();
                alertIncome.setTitle("¿Confirma liquidación?");
                alertIncome.setHeaderText("Ingresa "+formatter.format(income));
                alertIncome.setContentText("El aumento en precio es de "+formatter.format(ganances));
            }

            Optional<ButtonType> liquidationResult = alertIncome.showAndWait();
            if(liquidationResult.isPresent() && liquidationResult.get() == ButtonType.OK){

                String saleObservations = txtObservations.getText();
                if(saleObservations.length() > 0){
                    saleObservations = animal.getObservations() + "  |  " + saleObservations;
                }
                String description = "Venta animal con número: " + animal.getNumber() + " y Id: " + animal.getId();
                if(owner.getPercentage() != 0){
                    description += "  |  Animal en aumento, ganancia para el dueño: " + ownerIncome;
                }
                Date date = new Date(dpSaleDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                animal.setSaleWeight(Double.parseDouble(txtSaleWeight.getText()));
                animal.setSalePrice(Double.parseDouble(txtSalePrice.getText()));
                animal.setSaleDate(date);
                boolean response = model.sellAnimal(animal, income, date, description, saleObservations);


                if(response){
                    Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION, "Éxito");
                    alertSuccess.setHeaderText("Éxito liquidando el animal");
                    alertSuccess.showAndWait();
                    Stage currentStage = (Stage) txtSalePrice.getScene().getWindow();
                    showInventoryController.getAnimals();
                    currentStage.close();
                } else {
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                    alertError.setTitle("Error");
                    alertError.setHeaderText("No se pudo liquidar el animal");
                    alertError.showAndWait();
                }
            }
        }

    }
}

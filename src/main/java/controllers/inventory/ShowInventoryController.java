package controllers.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Model;
import models.interfaces.Animal;
import models.interfaces.Owner;
import models.interfaces.StateAnimal;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ShowInventoryController implements Initializable {
    private Model model;

    @FXML
    private ComboBox cbOwnerFiltrer;
    @FXML
    private ComboBox cbSexFilter;
    @FXML
    private ComboBox cbStateFilter;
    @FXML
    private DatePicker dpDateFrom;
    @FXML
    private DatePicker dpDateTo;
    @FXML
    private RadioButton rbPurchaseDateFilter;
    @FXML
    private RadioButton rbSaleDateFilter;
    @FXML
    private TableView<Animal> tblAnimals;
    @FXML
    private TableColumn<Animal, String> colNumber;
    @FXML
    private TableColumn<Animal, String> colOwner;
    @FXML
    private TableColumn<Animal, Integer> colAgeMonths;
    @FXML
    private TableColumn<Animal, String> colColor;
    @FXML
    private TableColumn<Animal, String> colIronBrand;
    @FXML
    private TableColumn<Animal, Character> colSex;
    @FXML
    private TableColumn<Animal, Double> colPurchaseWeight;
    @FXML
    private TableColumn<Animal, Double> colPurchasePrice;
    @FXML
    private TableColumn<Animal, Date> colPurchaseDate;
    @FXML
    private TableColumn<Animal, String> colObservations;
    @FXML
    private TableColumn<Animal, Double> colSaleWeight;
    @FXML
    private TableColumn<Animal, Double> colSalePrice;
    @FXML
    private TableColumn<Animal, Date> colSaleDate;
    @FXML
    private TableColumn<Animal, String> colState;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNumber.setCellValueFactory(new PropertyValueFactory("number"));
        colOwner.setCellValueFactory(new PropertyValueFactory("ownerInformation"));
        colAgeMonths.setCellValueFactory(new PropertyValueFactory("months"));
        colColor.setCellValueFactory(new PropertyValueFactory("color"));
        colIronBrand.setCellValueFactory(new PropertyValueFactory("ironBrand"));
        colSex.setCellValueFactory(new PropertyValueFactory("sex"));
        colPurchaseWeight.setCellValueFactory(new PropertyValueFactory("purchaseWeight"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory("purchasePrice"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory("purchaseDate"));
        colObservations.setCellValueFactory(new PropertyValueFactory("observations"));
        colSaleWeight.setCellValueFactory(new PropertyValueFactory("saleWeight"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory("salePrice"));
        colSaleDate.setCellValueFactory(new PropertyValueFactory("saleDate"));
        colState.setCellValueFactory(new PropertyValueFactory("state"));

        tblAnimals.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblAnimals.getSelectionModel().getSelectedItem() != null) {
                TablePosition<Animal, ?> pos = tblAnimals.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn<Animal, ?> col = pos.getTableColumn();
                Animal selectedAnimal = tblAnimals.getItems().get(row);
                if (col == colObservations) {
                    String observations = selectedAnimal.getObservations();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Observaciones del animal");
                    alert.setHeaderText("Observaciones del animal " + selectedAnimal.getNumber());
                    alert.setContentText(observations);
                    alert.showAndWait();
                    return;
                }
                if(col == colOwner){
                    Owner owner = selectedAnimal.getOwner();
                    String observations = "Id dueño: " + owner.getId() + "\nNombre: " + owner.getName() +
                            "\nPorcentaje: " + owner.getPercentage() + "\nMarca Hierro: " + owner.getIronBrand();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Dueño del animal");
                    alert.setHeaderText("Dueño del animal " + selectedAnimal.getNumber());
                    alert.setContentText(observations);
                    alert.showAndWait();
                }
            }
        });

        model = new Model();

        model.getOwnersInformation(cbOwnerFiltrer);
        cbSexFilter.setItems(FXCollections.observableArrayList("Macho", "Hembra"));
        cbStateFilter.setItems(FXCollections.observableArrayList(StateAnimal.active.toString(), StateAnimal.sold.toString(), StateAnimal.death.toString()));

        ObservableList<Animal> animals = model.getActiveAnimals();
        tblAnimals.setItems(animals);
    }

    public void selectPurchaseDateFilter(ActionEvent event) {
        if(rbSaleDateFilter.isSelected()){
            rbSaleDateFilter.setSelected(false);
        }
    }

    public void selectSaleDateFilter(ActionEvent event) {
        if(rbPurchaseDateFilter.isSelected()){
            rbPurchaseDateFilter.setSelected(false);
        }
    }
}

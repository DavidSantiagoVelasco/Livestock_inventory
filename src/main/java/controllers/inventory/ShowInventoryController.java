package controllers.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.Animal;
import models.interfaces.AnimalState;
import models.interfaces.FilterCard;
import models.interfaces.Owner;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class ShowInventoryController implements Initializable {

    private Model model;

    @FXML
    private TextField txtNumberFilter;
    @FXML
    private ComboBox cbOwnerFilter;
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
    @FXML
    private HBox hbFiltersContainer;
    @FXML
    private Button btnSellAnimal;
    @FXML
    private Button btnDeleteAnimal;

    private final List<Animal> animals = new ArrayList<>();
    private final List<FilterCard> filters = new ArrayList<>();
    private  LocalDate dateFrom = null;
    private LocalDate dateTo = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNumber.setCellValueFactory(new PropertyValueFactory<>("number"));
        colOwner.setCellValueFactory(new PropertyValueFactory<>("ownerInformation"));
        colAgeMonths.setCellValueFactory(new PropertyValueFactory<>("months"));
        colColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        colIronBrand.setCellValueFactory(new PropertyValueFactory<>("ironBrand"));
        colSex.setCellValueFactory(new PropertyValueFactory<>("sex"));
        colPurchaseWeight.setCellValueFactory(new PropertyValueFactory<>("purchaseWeight"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        colObservations.setCellValueFactory(new PropertyValueFactory<>("observations"));
        colSaleWeight.setCellValueFactory(new PropertyValueFactory<>("saleWeight"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        colSaleDate.setCellValueFactory(new PropertyValueFactory<>("saleDate"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));

        txtNumberFilter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                selectNumberFilter();
            }
        });

        tblAnimals.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblAnimals.getSelectionModel().getSelectedItem() != null) {
                TablePosition pos = tblAnimals.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn col = pos.getTableColumn();
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

        tblAnimals.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnSellAnimal.setVisible(true);
                btnDeleteAnimal.setVisible(true);
            } else {
                btnSellAnimal.setVisible(false);
                btnDeleteAnimal.setVisible(false);
            }
        });

        model = new Model();

        model.setOwnersInformation(cbOwnerFilter);
        cbSexFilter.setItems(FXCollections.observableArrayList("Macho", "Hembra"));
        cbStateFilter.setItems(FXCollections.observableArrayList(AnimalState.active.toString(), AnimalState.sold.toString(), AnimalState.death.toString()));

        setTblAnimals();
    }

    private void setTblAnimals() {
        ObservableList<Animal> animals = model.getActiveAnimals();
        setTblAnimals(animals);
    }

    private void setTblAnimals(ObservableList<Animal> animals){
        tblAnimals.setItems(animals);
        this.animals.clear();
        this.animals.addAll(animals);
    }

    @FXML
    private void selectPurchaseDateFilter() {
        if (dpDateFrom.getValue() == null || dpDateTo.getValue() == null) {
            rbPurchaseDateFilter.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Debes seleccionar primero el rango de fechas");
            alert.showAndWait();
            return;
        }
        if (rbSaleDateFilter.isSelected()) {
            removeFilterDate(false);
            rbSaleDateFilter.setSelected(false);
        }
        if (rbPurchaseDateFilter.isSelected()) {
            if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
                rbPurchaseDateFilter.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
                alert.showAndWait();
                dpDateTo.setValue(null);
                return;
            }
            addFilter("FilterDate", "Filtrar por fecha compra", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else {
            removeFilterDate(true);
        }
    }

    @FXML
    private void selectSaleDateFilter() {
        if (dpDateFrom.getValue() == null || dpDateTo.getValue() == null) {
            rbSaleDateFilter.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Debes seleccionar primero el rango de fechas");
            alert.showAndWait();
            return;
        }
        if (rbPurchaseDateFilter.isSelected()) {
            removeFilterDate(false);
            rbPurchaseDateFilter.setSelected(false);
        }
        if (rbSaleDateFilter.isSelected()) {
            if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
                rbSaleDateFilter.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
                alert.showAndWait();
                dpDateTo.setValue(null);
                return;
            }
            addFilter("FilterDate", "Filtrar por fecha venta", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else {
            removeFilterDate(true);
        }
    }

    private void removeFilterDate(boolean setDatesNull){
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard: filters
             ) {
            if(filterCard.getType().equals("FilterDate")){
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if(currentFilterCard == null){
            return;
        }
        filters.remove(currentFilterCard);
        if(setDatesNull){
            dpDateFrom.setValue(null);
            dpDateTo.setValue(null);
        }
    }

    private void addFilter(String filterType, String tittle, String information){

        AnchorPane filterCardOwner = new AnchorPane();
        filterCardOwner.setPrefSize(200, 200);

        Pane contentPane = new Pane();
        contentPane.setPrefSize(200, 62);
        contentPane.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 5; -fx-background-insets: 10 10 10 10;");

        Label filterLabelName = new Label(tittle);
        filterLabelName.setLayoutX(14);
        filterLabelName.setLayoutY(14);
        contentPane.getChildren().add(filterLabelName);

        Label filterLabelInformation = new Label(information);
        filterLabelInformation.setLayoutX(14);
        filterLabelInformation.setLayoutY(31);
        filterLabelInformation.setStyle("-fx-text-fill: #000000b2; -fx-font-size: 10;");
        contentPane.getChildren().add(filterLabelInformation);

        Button closeButton = new Button("X");
        closeButton.setLayoutX(166);
        closeButton.setLayoutY(12);
        closeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand");
        closeButton.setTextFill(Color.RED);
        closeButton.setFont(new Font(10));

        closeButton.setOnAction(e -> removeFilter(filterType, filterCardOwner));

        contentPane.getChildren().add(closeButton);
        filterCardOwner.getChildren().add(contentPane);

        hbFiltersContainer.getChildren().add(filterCardOwner);
        if(filterType.equals("FilterNumber")){
            FilterCard filterCard = new FilterCard("FilterNumber", filterCardOwner);
            filterCard.setValue(information);
            filters.add(filterCard);
            return;
        }
        filters.add(new FilterCard(filterType, filterCardOwner));
    }

    private void removeFilter(String filterType, AnchorPane filterCard){
        hbFiltersContainer.getChildren().remove(filterCard);
        FilterCard currentFilterCard = null;
        for (FilterCard fc :
                filters) {
            if(fc.getCard().equals(filterCard)){
                currentFilterCard = fc;
            }
        }
        if(currentFilterCard != null){
            filters.remove(currentFilterCard);
        }
        switch (filterType) {
            case "FilterNumber" -> txtNumberFilter.setText("");
            case "FilterOwner" -> cbOwnerFilter.setValue(null);
            case "FilterSex" -> cbSexFilter.setValue(null);
            case "FilterState" -> cbStateFilter.setValue(null);
            case "FilterDate" -> {
                dpDateTo.setValue(null);
                dpDateFrom.setValue(null);
                rbSaleDateFilter.setSelected(false);
                rbPurchaseDateFilter.setSelected(false);
            }
        }
    }

    @FXML
    private void selectOwnerFilter() {
        if (cbOwnerFilter.getValue() == null) {
            return;
        }
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard : filters
        ) {
            if (filterCard.getType().equals("FilterOwner")) {
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if(currentFilterCard != null){
            filters.remove(currentFilterCard);
        }
        String[] split = cbOwnerFilter.getValue().toString().split(" \\| Porcentaje:");
        String ownerInformation = split[0];
        if (ownerInformation.length() > 35) {
            ownerInformation = ownerInformation.substring(0, 35);
        }
        addFilter("FilterOwner", "Filtrar por dueño", ownerInformation);
    }

    @FXML
    private void selectSexFilter() {
        if (cbSexFilter.getValue() == null) {
            return;
        }
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard : filters
        ) {
            if (filterCard.getType().equals("FilterSex")) {
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if (currentFilterCard != null) {
            filters.remove(currentFilterCard);
        }
        addFilter("FilterSex", "Filtrar por sexo", cbSexFilter.getValue().toString());
    }

    @FXML
    private void selectStateFilter() {
        if (cbStateFilter.getValue() == null) {
            return;
        }
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard : filters
        ) {
            if (filterCard.getType().equals("FilterState")) {
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if(currentFilterCard != null){
            filters.remove(currentFilterCard);
        }
        addFilter("FilterState", "Filtrar por estado", cbStateFilter.getValue().toString());
    }

    private void selectNumberFilter() {
        if (txtNumberFilter.getText().length() == 0) {
            return;
        }
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard : filters
        ) {
            if (filterCard.getType().equals("FilterNumber")) {
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if (currentFilterCard != null) {
            filters.remove(currentFilterCard);
        }
        addFilter("FilterNumber", "Filtrar por número", txtNumberFilter.getText());
    }

    @FXML
    private void filter() {
        if (cbOwnerFilter.getValue() == null && cbSexFilter.getValue() == null && cbStateFilter.getValue() == null &&
                (!rbPurchaseDateFilter.isSelected() && !rbSaleDateFilter.isSelected()) && txtNumberFilter.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No hay filtros");
            alert.setHeaderText("Debes agregar filtros para poder completar la acción");
            alert.showAndWait();
            return;
        }
        if (txtNumberFilter.getText().length() > 0) {
            FilterCard existsFilterCard = null;
            for (FilterCard filterCard:
                 filters) {
                if(filterCard.getType().equals("FilterNumber")){
                    existsFilterCard = filterCard;
                }
            }
            if(existsFilterCard == null){
                addFilter("FilterNumber", "Filtrar por número", txtNumberFilter.getText());
            } else if (!existsFilterCard.getValue().equals(txtNumberFilter.getText())) {
                hbFiltersContainer.getChildren().remove(existsFilterCard.getCard());
                filters.remove(existsFilterCard);
                addFilter("FilterNumber", "Filtrar por número", txtNumberFilter.getText());
            }
        }
        int idOwner = cbOwnerFilter.getValue() != null ? model.getOwnerIdFromOwnerInformation(cbOwnerFilter.getValue().toString()) : -1;
        String sex = cbSexFilter.getValue() != null ? cbSexFilter.getValue().toString().charAt(0)+"" : "";
        String stateString = cbStateFilter.getValue() != null ? cbStateFilter.getValue().toString() : "";
        AnimalState animalState = null;
        if(stateString.length() > 0){
            switch (stateString) {
                case "Activo" -> animalState = AnimalState.active;
                case "Vendido" -> animalState = AnimalState.sold;
                case "Muerto" -> animalState = AnimalState.death;
            }
        }
        String purchaseOrSale = "";
        if (rbPurchaseDateFilter.isSelected()) {
            purchaseOrSale = "purchase";
        } else if (rbSaleDateFilter.isSelected()) {
            purchaseOrSale = "sale";
        }
        String dateFrom = dpDateFrom.getValue() != null ? dpDateFrom.getValue().toString() : "";
        String dateTo = dpDateTo.getValue() != null ? dpDateTo.getValue().toString() : "";
        ObservableList<Animal> animals = model.getFilterAnimals(txtNumberFilter.getText(), idOwner, sex, animalState, purchaseOrSale, dateFrom, dateTo);
        setTblAnimals(animals);
    }

    @FXML
    private void getAllAnimals() {
        ObservableList<Animal> animals = model.getAllAnimals();
        setTblAnimals(animals);
        clearFilters();
    }

    public void getAnimals() {
        ObservableList<Animal> animals = model.getActiveAnimals();
        setTblAnimals(animals);
        clearFilters();
    }

    private void clearFilters(){
        filters.clear();
        txtNumberFilter.setText("");
        hbFiltersContainer.getChildren().clear();
        cbOwnerFilter.setValue(null);
        cbSexFilter.setValue(null);
        cbStateFilter.setValue(null);
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);
        rbSaleDateFilter.setSelected(false);
        rbPurchaseDateFilter.setSelected(false);
    }

    @FXML
    private void deleteAnimal() {
        if(tblAnimals.getSelectionModel().getSelectedItem() == null){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("No se encuentra ningún animal seleccionado");
            alertError.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro que desea eliminar el animal?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Animal animal = tblAnimals.getSelectionModel().getSelectedItem();
            if(animal.getState().toString().equals(AnimalState.death.toString())){
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                alertError.setTitle("Error");
                alertError.setHeaderText("El animal ya se encuentra eliminado");
                alertError.showAndWait();
                return;
            } else if(animal.getState().toString().equals(AnimalState.sold.toString())){
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                alertError.setTitle("Error");
                alertError.setHeaderText("El animal se encuentra vendido");
                alertError.showAndWait();
                return;
            }
            boolean response = model.deleteAnimal(animal);
            if(response){
                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setTitle("Éxito");
                alertConfirmation.setHeaderText("Éxito eliminando el animal");
                alertConfirmation.showAndWait();
                setTblAnimals();
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                alertError.setTitle("Error");
                alertError.setHeaderText("Ocurrió un error. Por favor intente más tarde");
                alertError.showAndWait();
            }
        }
    }

    @FXML
    private void sellAnimal() {
        if(tblAnimals.getSelectionModel().getSelectedItem() == null){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("No se encuentra ningún animal seleccionado");
            alertError.showAndWait();
            return;
        }
        Animal animal = tblAnimals.getSelectionModel().getSelectedItem();
        if(animal.getState().toString().equals(AnimalState.death.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("El animal ya se encuentra eliminado");
            alertError.showAndWait();
            return;
        } else if(animal.getState().toString().equals(AnimalState.sold.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("El animal se encuentra vendido");
            alertError.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/sellAnimal.fxml"));
            SellAnimalController sellAnimalController = new SellAnimalController(animal, this);
            loader.setController(sellAnimalController);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Vender animal");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void selectDatePicker(ActionEvent event) {
        DatePicker datePicker = (DatePicker) event.getSource();
        if ((!rbSaleDateFilter.isSelected() && !rbPurchaseDateFilter.isSelected()) || dpDateFrom.getValue() == null
                || dpDateTo.getValue() == null) {
            updateDateValues(datePicker);
            return;
        }
        if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
            alert.showAndWait();
            if(datePicker == dpDateFrom){
                dpDateFrom.setValue(dateFrom);
            } else if (datePicker == dpDateTo) {
                dpDateTo.setValue(dateTo);
            }
            return;
        }
        removeFilterDate(false);
        if(rbPurchaseDateFilter.isSelected()){
            addFilter("FilterDate", "Filtrar por fecha compra", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else if (rbSaleDateFilter.isSelected()) {
            addFilter("FilterDate", "Filtrar por fecha venta", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        }
        updateDateValues(datePicker);
    }

    @FXML
    private void print() {
        if(animals.size() == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("No hay animales filtrados para hacer un reporte");
            alert.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/reportOptions.fxml"));
            ReportOptionsController reportOptionsController = new ReportOptionsController(animals);
            loader.setController(reportOptionsController);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();
            Alert alert;
            if(reportOptionsController.getResponse()){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("Se generó el reporte. Busque el pdf en descargas");
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Ocurrió un error. Intente más tarde");
            }
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDateConsistency(LocalDate fromDate, LocalDate toDate) {
        Date from = Date.valueOf(fromDate);
        Date to = Date.valueOf(toDate);
        return !to.before(from);
    }

    private void updateDateValues(DatePicker datePicker){
        if(datePicker == dpDateFrom){
            dateFrom = datePicker.getValue();
        } else if (datePicker == dpDateTo) {
            dateTo = datePicker.getValue();
        }
    }
}

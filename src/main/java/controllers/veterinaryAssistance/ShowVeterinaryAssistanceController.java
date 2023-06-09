package controllers.veterinaryAssistance;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.EventState;
import models.interfaces.FilterCard;
import models.interfaces.VeterinaryAssistance;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowVeterinaryAssistanceController implements Initializable {

    private final Model model = new Model();

    @FXML
    private ComboBox<String> cbStateFilter;
    @FXML
    private DatePicker dpDateFrom;
    @FXML
    private DatePicker dpDateTo;
    @FXML
    private RadioButton rbAssignedDateFilter;
    @FXML
    private RadioButton rbCompletedDateFilter;
    @FXML
    private RadioButton rbNextDateFilter;
    @FXML
    private Button btnCompleteVeterinaryAssistance;
    @FXML
    private Button btnCancelVeterinaryAssistance;
    @FXML
    private HBox hbFiltersContainer;
    @FXML
    private TableView<VeterinaryAssistance> tblVeterinaryAssistance;
    @FXML
    private TableColumn<VeterinaryAssistance, Date> colAssignedDate;
    @FXML
    private TableColumn<VeterinaryAssistance, Date> colCompletionDate;
    @FXML
    private TableColumn<VeterinaryAssistance, String> colName;
    @FXML
    private TableColumn<VeterinaryAssistance, String> colDescription;
    @FXML
    private TableColumn<VeterinaryAssistance, Double> colCost;
    @FXML
    private TableColumn<VeterinaryAssistance, Date> colNextDate;
    @FXML
    private TableColumn<VeterinaryAssistance, EventState> colState;

    private final List<FilterCard> filters = new ArrayList<>();
    private LocalDate dateFrom = null;
    private LocalDate dateTo = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colAssignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        colCompletionDate.setCellValueFactory(new PropertyValueFactory<>("completionDate"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colNextDate.setCellValueFactory(new PropertyValueFactory<>("nextDate"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));

        tblVeterinaryAssistance.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblVeterinaryAssistance.getSelectionModel().getSelectedItem() != null) {
                TablePosition<VeterinaryAssistance, ?> pos = tblVeterinaryAssistance.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn<VeterinaryAssistance, ?> col = pos.getTableColumn();
                VeterinaryAssistance veterinaryAssistance = tblVeterinaryAssistance.getItems().get(row);
                if (col == colDescription) {
                    String description = veterinaryAssistance.getDescription();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Descripción de la asistencia veterinaria");
                    alert.setContentText(description);
                    alert.showAndWait();
                }
            }
        });

        tblVeterinaryAssistance.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnCancelVeterinaryAssistance.setVisible(true);
                btnCompleteVeterinaryAssistance.setVisible(true);
            } else {
                btnCancelVeterinaryAssistance.setVisible(false);
                btnCompleteVeterinaryAssistance.setVisible(false);
            }
        });

        cbStateFilter.setItems(FXCollections.observableArrayList(EventState.active.toString(), EventState.complete.toString(), EventState.canceled.toString()));
        getVeterinaryAssistance();
    }

    @FXML
    private void selectStateFilter() {
        if(cbStateFilter.getValue() == null){
            return;
        }
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard: filters
        ) {
            if(filterCard.getType().equals("FilterState")){
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if(currentFilterCard != null){
            filters.remove(currentFilterCard);
        }
        addFilter("FilterState", "Filtrar por estado", cbStateFilter.getValue());
    }

    @FXML
    private void selectDateFilter(ActionEvent event) {
        if (dpDateFrom.getValue() == null || dpDateTo.getValue() == null) {
            RadioButton selectedRadioButton = (RadioButton) event.getSource();
            selectedRadioButton.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Debes seleccionar primero el rango de fechas");
            alert.showAndWait();
            return;
        }
        RadioButton source = (RadioButton) event.getSource();
        if (!source.isSelected()) {
            removeFilterDate(true);
            return;
        } else {
            if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
                source.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
                alert.showAndWait();
                dpDateTo.setValue(null);
                return;
            }
        }
        if (source == rbAssignedDateFilter) {
            removeFilterDate(false);
            rbCompletedDateFilter.setSelected(false);
            rbNextDateFilter.setSelected(false);
            addFilter("FilterDate", "Filtrar por fecha asignada", "Desde: " + dpDateFrom.getValue().toString()
                    + " | Hasta: " + dpDateTo.getValue().toString());
        } else if (source == rbCompletedDateFilter) {
            removeFilterDate(false);
            rbAssignedDateFilter.setSelected(false);
            rbNextDateFilter.setSelected(false);
            addFilter("FilterDate", "Filtrar por fecha completada", "Desde: " + dpDateFrom.getValue().toString()
            + " | Hasta: " + dpDateTo.getValue().toString());
        } else if (source == rbNextDateFilter) {
            removeFilterDate(false);
            rbAssignedDateFilter.setSelected(false);
            rbCompletedDateFilter.setSelected(false);
            addFilter("FilterDate", "Filtrar por siguiente fecha", "Desde: " + dpDateFrom.getValue().toString()
            + " | Hasta: " + dpDateTo.getValue().toString());
        }
    }

    @FXML
    private void filter() {
        if(cbStateFilter.getValue() == null && (!rbAssignedDateFilter.isSelected() && !rbCompletedDateFilter.isSelected() && !rbNextDateFilter.isSelected())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No hay filtros");
            alert.setHeaderText("Debes agregar filtros para poder completar la acción");
            alert.showAndWait();
            return;
        }
        String stateString = cbStateFilter.getValue() != null ? cbStateFilter.getValue() : "";
        EventState eventState = null;
        if(stateString.length() > 0){
            switch (stateString) {
                case "Activo" -> eventState = EventState.active;
                case "Completado" -> eventState = EventState.complete;
                case "Cancelado" -> eventState = EventState.canceled;
            }
        }
        String dateFilterType = "";
        if (rbCompletedDateFilter.isSelected()) {
            dateFilterType = "completion";
        } else if (rbAssignedDateFilter.isSelected()) {
            dateFilterType = "assigned";
        } else if (rbNextDateFilter.isSelected()){
            dateFilterType = "next";
        }
        String dateFrom = dpDateFrom.getValue() != null ? dpDateFrom.getValue().toString() : "";
        String dateTo = dpDateTo.getValue() != null ? dpDateTo.getValue().toString() : "";
        ObservableList<VeterinaryAssistance> veterinaryAssistance = model.getFilterVeterinaryAssistance(eventState, dateFilterType, dateFrom, dateTo);
        tblVeterinaryAssistance.setItems(veterinaryAssistance);
    }

    @FXML
    private void getAllVeterinaryAssistance() {
        ObservableList<VeterinaryAssistance> veterinaryAssistance = model.getAllVeterinaryAssistance();
        tblVeterinaryAssistance.setItems(veterinaryAssistance);
        clearFilters();
    }

    public void getVeterinaryAssistance() {
        ObservableList<VeterinaryAssistance> veterinaryAssistance = model.getActiveVeterinaryAssistance();
        tblVeterinaryAssistance.setItems(veterinaryAssistance);
        clearFilters();
    }

    @FXML
    private void completeVeterinaryAssistance() {
        if(tblVeterinaryAssistance.getSelectionModel().getSelectedItem() == null){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("No se encuentra ninguna asistencia veterinaria seleccionada");
            alertError.showAndWait();
            return;
        }
        VeterinaryAssistance veterinaryAssistance = tblVeterinaryAssistance.getSelectionModel().getSelectedItem();
        if(veterinaryAssistance.getState().toString().equals(EventState.complete.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("La asistencia veterinaria ya se encuentra completada");
            alertError.showAndWait();
            return;
        } else if(veterinaryAssistance.getState().toString().equals(EventState.canceled.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("La asistencia veterinaria se encuentra cancelada");
            alertError.showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/veterinaryAssistance/completeVeterinaryAssistance.fxml"));
            CompleteVeterinaryAssistanceController completeVeterinaryAssistanceController =
                    new CompleteVeterinaryAssistanceController(veterinaryAssistance, this);
            loader.setController(completeVeterinaryAssistanceController);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelVeterinaryAssistance() {
        if(tblVeterinaryAssistance.getSelectionModel().getSelectedItem() == null){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("No se encuentra ninguna asistencia veterinaria seleccionada");
            alertError.showAndWait();
            return;
        }
        VeterinaryAssistance veterinaryAssistance = tblVeterinaryAssistance.getSelectionModel().getSelectedItem();
        if(veterinaryAssistance.getState().toString().equals(EventState.complete.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("La asistencia veterinaria se encuentra completada");
            alertError.showAndWait();
            return;
        } else if(veterinaryAssistance.getState().toString().equals(EventState.canceled.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("La asistencia veterinaria ya se encuentra cancelada");
            alertError.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de querer cancelar la asistencia veterinaria?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            boolean response = model.cancelVeterinaryAssistance(veterinaryAssistance);
            if(response){
                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setTitle("Éxito");
                alertConfirmation.setHeaderText("Éxito cancelando la asistencia veterinaria");
                alertConfirmation.showAndWait();
                getVeterinaryAssistance();
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                alertError.setTitle("Error");
                alertError.setHeaderText("Ocurrió un error. Por favor intente más tarde");
                alertError.showAndWait();
            }
        }
    }

    @FXML
    private void selectDatePicker(ActionEvent event) {
        DatePicker datePicker = (DatePicker) event.getSource();
        if ((!rbAssignedDateFilter.isSelected() && !rbCompletedDateFilter.isSelected() &&
                !rbNextDateFilter.isSelected()) || dpDateFrom.getValue() == null || dpDateTo.getValue() == null) {
            updateDateValues(datePicker);
            return;
        }
        if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
            alert.showAndWait();
            if (datePicker == dpDateFrom) {
                dpDateFrom.setValue(dateFrom);
            } else if (datePicker == dpDateTo) {
                dpDateTo.setValue(dateTo);
            }
            return;
        }
        removeFilterDate(false);
        if (rbAssignedDateFilter.isSelected()) {
            addFilter("FilterDate", "Filtrar por fecha asignada", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else if (rbCompletedDateFilter.isSelected()) {
            addFilter("FilterDate", "Filtrar por fecha completada", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else if (rbNextDateFilter.isSelected()) {
            addFilter("FilterDate", "Filtrar por siguiente fecha", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        }
        updateDateValues(datePicker);
    }

    private void removeFilterDate(boolean setDatesNull) {
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard : filters
        ) {
            if (filterCard.getType().equals("FilterDate")) {
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if (currentFilterCard == null) {
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
        filterCardOwner.setPrefSize(230, 230);

        Pane contentPane = new Pane();
        contentPane.setPrefSize(230, 62);
        contentPane.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-background-radius: 5; -fx-background-insets: 10 10 10 10;");

        Label filterLabelName = new Label(tittle);
        filterLabelName.setLayoutX(22);
        filterLabelName.setLayoutY(14);
        contentPane.getChildren().add(filterLabelName);

        Label filterLabelInformation = new Label(information);
        filterLabelInformation.setLayoutX(22);
        filterLabelInformation.setLayoutY(31);
        filterLabelInformation.setStyle("-fx-text-fill: #000000b2; -fx-font-size: 10;");
        contentPane.getChildren().add(filterLabelInformation);

        Button closeButton = new Button("X");
        closeButton.setLayoutX(194);
        closeButton.setLayoutY(12);
        closeButton.setStyle("-fx-background-color: transparent; -fx-cursor: hand");
        closeButton.setTextFill(Color.RED);
        closeButton.setFont(new Font(10));

        closeButton.setOnAction(e -> removeFilter(filterType, filterCardOwner));

        contentPane.getChildren().add(closeButton);
        filterCardOwner.getChildren().add(contentPane);

        hbFiltersContainer.getChildren().add(filterCardOwner);

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
            case "FilterState" -> cbStateFilter.setValue(null);
            case "FilterDate" -> {
                dpDateTo.setValue(null);
                dpDateFrom.setValue(null);
                rbAssignedDateFilter.setSelected(false);
                rbCompletedDateFilter.setSelected(false);
                rbNextDateFilter.setSelected(false);
            }
        }
    }

    private void clearFilters() {
        filters.clear();
        hbFiltersContainer.getChildren().clear();
        cbStateFilter.setValue(null);
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);
        rbCompletedDateFilter.setSelected(false);
        rbAssignedDateFilter.setSelected(false);
        rbNextDateFilter.setSelected(false);
    }

    private boolean checkDateConsistency(LocalDate fromDate, LocalDate toDate) {
        Date from = Date.valueOf(fromDate);
        Date to = Date.valueOf(toDate);
        return !to.before(from);
    }

    private void updateDateValues(DatePicker datePicker) {
        if (datePicker == dpDateFrom) {
            dateFrom = datePicker.getValue();
        } else if (datePicker == dpDateTo) {
            dateTo = datePicker.getValue();
        }
    }
}

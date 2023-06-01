package controllers.tasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import models.Model;
import models.interfaces.EventState;
import models.interfaces.FilterCard;
import models.interfaces.Task;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ShowTasksController implements Initializable {

    private Model model;

    @FXML
    private ComboBox cbStateFilter;
    @FXML
    private DatePicker dpDateFrom;
    @FXML
    private DatePicker dpDateTo;
    @FXML
    private RadioButton rbCreationDateFilter;
    @FXML
    private RadioButton rbAssignedDateFilter;
    @FXML
    private TableView<Task> tblTasks;
    @FXML
    private TableColumn<Task, String> colName;
    @FXML
    private TableColumn<Task, String> colDescription;
    @FXML
    private TableColumn<Task, Date> colCreationDate;
    @FXML
    private TableColumn<Task, Date> colAssignedDate;
    @FXML
    private TableColumn<Task, String> colState;
    @FXML
    private HBox hbFiltersContainer;
    @FXML
    private Button btnCompleteTask;
    @FXML
    private Button btnCancelTask;

    private final List<FilterCard> filters = new ArrayList<>();
    private  LocalDate dateFrom = null;
    private LocalDate dateTo = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        model = new Model();

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        colAssignedDate.setCellValueFactory(new PropertyValueFactory<>("assignedDate"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));

        tblTasks.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblTasks.getSelectionModel().getSelectedItem() != null) {
                TablePosition pos = tblTasks.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn col = pos.getTableColumn();
                Task selectedTask = tblTasks.getItems().get(row);
                if (col == colDescription) {
                    String description = selectedTask.getDescription();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Descripción de la tarea");
                    alert.setContentText(description);
                    alert.showAndWait();
                }
            }
        });

        tblTasks.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                btnCompleteTask.setVisible(true);
                btnCancelTask.setVisible(true);
            } else {
                btnCompleteTask.setVisible(false);
                btnCancelTask.setVisible(false);
            }
        });

        cbStateFilter.setItems(FXCollections.observableArrayList(EventState.active.toString(), EventState.complete.toString(), EventState.canceled.toString()));
        setTblTasks();
    }

    private void setTblTasks(){
        ObservableList<Task> tasks = model.getActiveTasks();
        tblTasks.setItems(tasks);
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
        addFilter("FilterState", "Filtrar por estado", cbStateFilter.getValue().toString());
    }

    @FXML
    private void selectCreationDateFilter() {
        if(dpDateFrom.getValue() == null || dpDateTo.getValue() == null){
            rbCreationDateFilter.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Debes seleccionar primero el rango de fechas");
            alert.showAndWait();
            return;
        }
        if(rbAssignedDateFilter.isSelected()){
            removeFilterDate(false);
            rbAssignedDateFilter.setSelected(false);
        }
        if(rbCreationDateFilter.isSelected()) {
            if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
                rbCreationDateFilter.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
                alert.showAndWait();
                dpDateTo.setValue(null);
                return;
            }
            addFilter("FilterDate", "Filtrar por fecha creación", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else {
            removeFilterDate(true);
        }
    }

    @FXML
    private void selectAssignedDateFilter() {
        if(dpDateFrom.getValue() == null || dpDateTo.getValue() == null){
            rbAssignedDateFilter.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Debes seleccionar primero el rango de fechas");
            alert.showAndWait();
            return;
        }
        if(rbCreationDateFilter.isSelected()){
            removeFilterDate(false);
            rbCreationDateFilter.setSelected(false);
        }
        if(rbAssignedDateFilter.isSelected()) {
            if (!checkDateConsistency(dpDateFrom.getValue(), dpDateTo.getValue())) {
                rbAssignedDateFilter.setSelected(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("La fecha desde no puede ser mayor que la fecha hasta");
                alert.showAndWait();
                dpDateTo.setValue(null);
                return;
            }
            addFilter("FilterDate", "Filtrar por fecha asignada", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else {
            removeFilterDate(true);
        }
    }

    @FXML
    private void getAllTasks() {
        ObservableList<Task> tasks = model.getAllTasks();
        tblTasks.setItems(tasks);
        clearFilters();
    }

    @FXML
    private void filter() {
        if(cbStateFilter.getValue() == null && (!rbAssignedDateFilter.isSelected() && !rbCreationDateFilter.isSelected())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No hay filtros");
            alert.setHeaderText("Debes agregar filtros para poder completar la acción");
            alert.showAndWait();
            return;
        }
        String stateString = cbStateFilter.getValue() != null ? cbStateFilter.getValue().toString() : "";
        EventState eventState = null;
        if(stateString.length() > 0){
            switch (stateString) {
                case "Activo" -> eventState = EventState.active;
                case "Completado" -> eventState = EventState.complete;
                case "Cancelado" -> eventState = EventState.canceled;
            }
        }
        String creationOrAssigned = "";
        if (rbCreationDateFilter.isSelected()) {
            creationOrAssigned = "creation";
        } else if (rbAssignedDateFilter.isSelected()) {
            creationOrAssigned = "assigned";
        }
        String dateFrom = dpDateFrom.getValue() != null ? dpDateFrom.getValue().toString() : "";
        String dateTo = dpDateTo.getValue() != null ? dpDateTo.getValue().toString() : "";
        ObservableList<Task> tasks = model.getFilterTasks(eventState, creationOrAssigned, dateFrom, dateTo);
        tblTasks.setItems(tasks);
    }

    @FXML
    private void getTasks() {
        ObservableList<Task> tasks = model.getActiveTasks();
        tblTasks.setItems(tasks);
        clearFilters();
    }

    @FXML
    private void clearFilters() {
        filters.clear();
        hbFiltersContainer.getChildren().clear();
        cbStateFilter.setValue(null);
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);
        rbCreationDateFilter.setSelected(false);
        rbAssignedDateFilter.setSelected(false);
    }

    @FXML
    private void completeTask() {
        if(tblTasks.getSelectionModel().getSelectedItem() == null){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("No se encuentra ningún recordatorio seleccionado");
            alertError.showAndWait();
            return;
        }
        Task task = tblTasks.getSelectionModel().getSelectedItem();
        if(task.getState().toString().equals(EventState.complete.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("El recordatorio ya se encuentra completado");
            alertError.showAndWait();
            return;
        } else if(task.getState().toString().equals(EventState.canceled.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("El recordatorio se encuentra cancelado");
            alertError.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de querer completar el recordatorio?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            boolean response = model.completeTask(task);
            if(response){
                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setTitle("Éxito");
                alertConfirmation.setHeaderText("Éxito completando el recordatorio");
                alertConfirmation.showAndWait();
                setTblTasks();
            } else {
                Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                alertError.setTitle("Error");
                alertError.setHeaderText("Ocurrió un error. Por favor intente más tarde");
                alertError.showAndWait();
            }
        }
    }

    @FXML
    private void cancelTask() {
        if(tblTasks.getSelectionModel().getSelectedItem() == null){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("No se encuentra ningún recordatorio seleccionado");
            alertError.showAndWait();
            return;
        }
        Task task = tblTasks.getSelectionModel().getSelectedItem();
        if(task.getState().toString().equals(EventState.complete.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("El recordatorio se encuentra completado");
            alertError.showAndWait();
            return;
        } else if(task.getState().toString().equals(EventState.canceled.toString())){
            Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
            alertError.setTitle("Error");
            alertError.setHeaderText("El recordatorio ya se encuentra cancelado");
            alertError.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro de querer cancelar el recordatorio?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            boolean response = model.cancelTask(task);
            if(response){
                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setTitle("Éxito");
                alertConfirmation.setHeaderText("Éxito cancelando el recordatorio");
                alertConfirmation.showAndWait();
                setTblTasks();
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
        if ((!rbCreationDateFilter.isSelected() && !rbAssignedDateFilter.isSelected()) || dpDateFrom.getValue() == null
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
        if(rbCreationDateFilter.isSelected()){
            addFilter("FilterDate", "Filtrar por fecha creación", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else if (rbAssignedDateFilter.isSelected()) {
            addFilter("FilterDate", "Filtrar por fecha asignada", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        }
        updateDateValues(datePicker);
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
                rbCreationDateFilter.setSelected(false);
                rbAssignedDateFilter.setSelected(false);
            }
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

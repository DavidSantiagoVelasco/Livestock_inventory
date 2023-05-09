package controllers.tasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.interfaces.*;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
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
    private List<FilterCard> filters = new ArrayList<>();

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
                TablePosition<Task, ?> pos = tblTasks.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn<Task, ?> col = pos.getTableColumn();
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

        cbStateFilter.setItems(FXCollections.observableArrayList(StateTask.active.toString(), StateTask.complete.toString(), StateTask.canceled.toString()));
        setTblTasks();
    }

    private void setTblTasks(){
        ObservableList<Task> tasks = model.getActiveTasks();
        tblTasks.setItems(tasks);
    }

    public void selectStateFilter() {
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

    public void selectCreationDateFilter() {
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
        if(rbCreationDateFilter.isSelected()){
            addFilter("FilterDate", "Filtrar por fecha creación", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else {
            removeFilterDate(true);
        }
    }

    public void selectAssignedDateFilter() {
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
        if(rbAssignedDateFilter.isSelected()){
            addFilter("FilterDate", "Filtrar por fecha asignada", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
        } else {
            removeFilterDate(true);
        }
    }

    public void getAllTasks() {
        ObservableList<Task> tasks = model.getAllTasks();
        tblTasks.setItems(tasks);
        clearFilters();
    }

    public void filter() {
    }

    public void getTasks() {
    }

    public void clearFilters() {
        filters.clear();
        hbFiltersContainer.getChildren().clear();
        cbStateFilter.setValue(null);
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);
        rbCreationDateFilter.setSelected(false);
        rbAssignedDateFilter.setSelected(false);
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

    public void completeTask() {
    }

    public void cancelTask() {
    }
}

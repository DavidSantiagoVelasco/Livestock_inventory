package controllers.finances;

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
import models.interfaces.FilterCard;
import models.interfaces.Finance;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ShowFinancesController implements Initializable {

    private final Model model = new Model();

    @FXML
    private ComboBox cbTypeFinance;
    @FXML
    private DatePicker dpDateFrom;
    @FXML
    private DatePicker dpDateTo;
    @FXML
    private TableView<Finance> tblFinances;
    @FXML
    private TableColumn<Finance, Double> colIncome;
    @FXML
    private TableColumn<Finance, Double> colExpense;
    @FXML
    private TableColumn<Finance, String> colDescription;
    @FXML
    private TableColumn<Finance, Date> colDate;
    @FXML
    private HBox hbFiltersContainer;

    private final List<FilterCard> filters = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colIncome.setCellValueFactory(new PropertyValueFactory<>("income"));
        colExpense.setCellValueFactory(new PropertyValueFactory<>("expense"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        tblFinances.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblFinances.getSelectionModel().getSelectedItem() != null) {
                TablePosition pos = tblFinances.getSelectionModel().getSelectedCells().get(0);
                int row = pos.getRow();
                TableColumn col = pos.getTableColumn();
                Finance selectedFinance = tblFinances.getItems().get(row);
                if (col == colDescription) {
                    String description = selectedFinance.getDescription();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Descripción del registro");
                    alert.setContentText(description);
                    alert.showAndWait();
                }
            }
        });

        cbTypeFinance.setItems(FXCollections.observableArrayList("Ingreso", "Egreso"));
        setTblFinances();

    }

    private void setTblFinances() {
        ObservableList<Finance> finances = model.getFinances();
        tblFinances.setItems(finances);
    }

    @FXML
    private void selectTypeFinanceFilter() {
        if(cbTypeFinance.getValue() == null){
            return;
        }
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard: filters
        ) {
            if(filterCard.getType().equals("FilterType")){
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if(currentFilterCard != null){
            filters.remove(currentFilterCard);
        }
        addFilter("FilterType", "Filtrar por tipo", cbTypeFinance.getValue().toString());
    }

    @FXML
    private void filter() {
    }

    @FXML
    private void getFinances() {
    }

    @FXML
    private void selectDatePicker() {
        if(dpDateTo.getValue() != null && dpDateFrom.getValue() != null){
            FilterCard currentFilterCard = null;
            for (FilterCard filterCard: filters
            ) {
                if(filterCard.getType().equals("FilterDate")){
                    hbFiltersContainer.getChildren().remove(filterCard.getCard());
                    currentFilterCard = filterCard;
                }
            }
            if(currentFilterCard != null){
                filters.remove(currentFilterCard);
            }
            addFilter("FilterDate", "Filtrar por fecha", "Desde: " +
                    dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
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
            case "FilterType" -> cbTypeFinance.setValue(null);
            case "FilterDate" -> {
                dpDateTo.setValue(null);
                dpDateFrom.setValue(null);
            }
        }
    }
}

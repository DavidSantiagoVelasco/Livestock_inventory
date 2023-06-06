package controllers.finances;

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
import models.interfaces.FilterCard;
import models.interfaces.Finance;
import models.responses.FilterFinancesResponse;

import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
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
    @FXML
    private TextArea txtTotalIncomes;
    @FXML
    private TextArea txtTotalExpenses;

    private final List<FilterCard> filters = new ArrayList<>();
    private LocalDate dateFrom = null;
    private LocalDate dateTo = null;


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
        txtTotalIncomes.setText("$0");
        txtTotalExpenses.setText("$0");
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
        if (cbTypeFinance.getValue() == null && (dpDateFrom.getValue() == null || dpDateTo.getValue() == null)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No hay filtros");
            alert.setHeaderText("Debes agregar filtros para poder completar la acción");
            alert.showAndWait();
            return;
        }
        String financesType = cbTypeFinance.getValue() != null ? cbTypeFinance.getValue().toString() : "";
        if (financesType.length() > 0) {
            switch (financesType) {
                case "Ingreso" -> financesType = "income";
                case "Egreso" -> financesType = "expense";
            }
        }
        String dateFrom = dpDateFrom.getValue() != null ? dpDateFrom.getValue().toString() : "";
        String dateTo = dpDateTo.getValue() != null ? dpDateTo.getValue().toString() : "";
        FilterFinancesResponse filterFinances = model.getFilterFinances(financesType, dateFrom, dateTo);
        if (filterFinances == null) {
            tblFinances.setItems(null);
            txtTotalIncomes.setText("$0");
            txtTotalExpenses.setText("$0");
            return;
        }
        tblFinances.setItems(filterFinances.getFinances());
        DecimalFormat decimalFormat = new DecimalFormat("$#,###.###");
        txtTotalIncomes.setText(decimalFormat.format(filterFinances.getTotalIncomes()));
        txtTotalExpenses.setText(decimalFormat.format(filterFinances.getTotalExpenses()));
    }

    @FXML
    private void getFinances() {
        ObservableList<Finance> finances = model.getFinances();
        tblFinances.setItems(finances);
        clearFilters();
    }

    @FXML
    private void selectDatePicker(ActionEvent event) {
        DatePicker datePicker = (DatePicker) event.getSource();
        if (dpDateFrom.getValue() == null || dpDateTo.getValue() == null) {
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
        FilterCard currentFilterCard = null;
        for (FilterCard filterCard : filters
        ) {
            if (filterCard.getType().equals("FilterDate")) {
                hbFiltersContainer.getChildren().remove(filterCard.getCard());
                currentFilterCard = filterCard;
            }
        }
        if (currentFilterCard != null) {
            filters.remove(currentFilterCard);
        }
        addFilter("FilterDate", "Filtrar por fecha", "Desde: " +
                dpDateFrom.getValue().toString() + " | Hasta: " + dpDateTo.getValue().toString());
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
            case "FilterType" -> cbTypeFinance.setValue(null);
            case "FilterDate" -> {
                dpDateTo.setValue(null);
                dpDateFrom.setValue(null);
            }
        }
    }

    private void clearFilters() {
        filters.clear();
        hbFiltersContainer.getChildren().clear();
        cbTypeFinance.setValue(null);
        dpDateTo.setValue(null);
        dpDateFrom.setValue(null);
        txtTotalIncomes.setText("$0");
        txtTotalExpenses.setText("$0");
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

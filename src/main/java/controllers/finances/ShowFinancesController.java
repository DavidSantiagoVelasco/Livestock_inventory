package controllers.finances;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Model;
import models.interfaces.Finance;

import java.net.URL;
import java.sql.Date;
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
    private HBox hbFiltersContainer;
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
    }

    @FXML
    private void filter() {
    }

    @FXML
    private void getFinances() {
    }
}

package controllers.inventory;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;

import java.net.URL;
import java.util.ResourceBundle;

public class ShowInventoryController implements Initializable {

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

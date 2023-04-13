package controllers.owners;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.Owner;

import java.net.URL;
import java.util.ResourceBundle;

public class OwnersController implements Initializable {

    private Model model;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPercentage;
    @FXML
    private Button btnNewOwner;
    @FXML
    private Button btnModifyOwners;
    @FXML
    private TableView<Owner> tblOwners;
    @FXML
    private TableColumn<Owner, String> colOwnerId;
    @FXML
    private TableColumn<Owner, String> colOwnerName;
    @FXML
    private TableColumn<Owner, String> colOwnerPercentage;
    @FXML
    private TableColumn<Owner, String> colOwnerState;

    @FXML
    public void newOwner(ActionEvent event) {
        if(this.txtName.getLength() == 0 || this.txtPercentage.getLength() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        Owner owner = this.model.createOwner(this.txtName.getText(), Double.parseDouble(this.txtPercentage.getText()));
        if(owner == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setHeaderText("Ocurrió un error. Intente más tarde");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR, "Éxito");
        alert.setHeaderText("Éxito creando el dueño");
        alert.showAndWait();
        this.getOwners();
    }

    @FXML
    private void modifyOwners(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/owners/modifyOwners.fxml"));
            Parent inventoryParent = loader.load();
            Scene inventoryScene = new Scene(inventoryParent);
            Stage stage = new Stage();

            stage.setScene(inventoryScene);
            stage.show();

        }catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void getOwners(){
        ObservableList<Owner> owners = this.model.getAllOwners();
        this.tblOwners.setItems(owners);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.colOwnerId.setCellValueFactory(new PropertyValueFactory("id"));
        this.colOwnerName.setCellValueFactory(new PropertyValueFactory("name"));
        this.colOwnerPercentage.setCellValueFactory(new PropertyValueFactory("percentage"));
        this.colOwnerState.setCellValueFactory(new PropertyValueFactory("active"));

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*\\.?\\d*")) {
                return change;
            } else {
                return null;
            }
        });

        this.txtPercentage.setTextFormatter(textFormatter);

        this.model = new Model();
        this.getOwners();
    }
}

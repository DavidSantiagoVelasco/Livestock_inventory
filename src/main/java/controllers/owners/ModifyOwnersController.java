package controllers.owners;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Pair;
import models.Model;
import models.interfaces.Animal;
import models.interfaces.Owner;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ModifyOwnersController implements Initializable {

    private Model model;

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPercentage;
    @FXML
    private ComboBox cbOwners;
    @FXML
    private Button btnModify;
    @FXML
    private RadioButton rbDelete;

    @FXML
    private void selectOwner(ActionEvent event) {
    }

    @FXML
    private void modifyOwner(ActionEvent event){
        if(cbOwners.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Seleccione el dueño");
            alert.setHeaderText("Debe seleccionar un dueño para poder continuar");
            alert.showAndWait();
            return;
        }
        if(rbDelete.isSelected()){
            confirmDeleteOwner();
            return;
        }
        if(txtName.getLength() == 0 && txtPercentage.getLength() == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Llena los campos");
            alert.setHeaderText("Faltan campos por llenar");
            alert.showAndWait();
            return;
        }
        confirmModifyOwner();
    }

    private void confirmModifyOwner(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro que desea modificar el dueño?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            modifyOwnerNameOrPercentage();
        }
    }

    private void modifyOwnerNameOrPercentage(){
        String selectedOwner = (String) cbOwners.getSelectionModel().getSelectedItem();
        int idSelectedOwner = getOwnerIdFromOwnerInformation(selectedOwner);
        Pair<Integer, Boolean> response;
        if(txtName.getLength() > 0){
            if(txtPercentage.getLength() > 0) {
                boolean status = model.modifyOwnerNameAndPercentage(idSelectedOwner, txtName.getText(), Double.parseDouble(txtPercentage.getText()));
                response = new Pair<>(0, status);
            } else {
                boolean status = model.modifyOwnerName(idSelectedOwner, txtName.getText());
                response = new Pair<>(1, status);
            }
        } else {
            boolean status = model.modifyOwnerPercentage(idSelectedOwner, Double.parseDouble(txtPercentage.getText()));
            response = new Pair<>(2, status);
        }
        if(!response.getValue()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo modificar el dueño");
            alert.showAndWait();
            return;
        }else if(response.getKey() == 0){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Exito modificando el nombre y porcentaje del dueño");
            alert.showAndWait();
        }else if(response.getKey() == 1){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Exito modificando el nombre del dueño");
            alert.showAndWait();
        }else if(response.getKey() == 2){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText("Exito modificando el porcentaje del dueño");
            alert.showAndWait();
        }
        restartValues();
        getOwnersInformation();
    }

    private void confirmDeleteOwner(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro que desea eliminar el dueño?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            deleteOwner();
        }
    }

    private void deleteOwner(){
        String selectedOwner = (String) cbOwners.getSelectionModel().getSelectedItem();
        int idSelected = getOwnerIdFromOwnerInformation(selectedOwner);
        ObservableList<Animal> animals = model.getAnimalsByOwnerId(idSelected);
        if(animals == null || animals.size() == 0){
            int status = model.deleteOwner(idSelected);
            if(status == 0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Exito eliminando el dueño");
                alert.showAndWait();
                getOwnersInformation();
            }else if (status == -1){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
                alert.setTitle("Error");
                alert.setHeaderText("Ocurrió un error. Por favor intente más tarde");
                alert.showAndWait();
            } else if (status == 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
                alert.setTitle("Error");
                alert.setHeaderText("No se pudo eliminar el dueño");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("El dueño cuenta con animales");
            alert.setHeaderText("Para poder eliminar a un dueño, este no debe tener animales activos");
            alert.showAndWait();
        }
        restartValues();
    }

    private void getOwnersInformation(){
        ObservableList<Owner> owners = model.getActiveOwners();
        ObservableList<String> ownersInformation = FXCollections.observableArrayList();
        for (Owner owner: owners) {
            ownersInformation.add("Id: " + owner.getId() + " | Nombre: " + owner.getName() + " | Porcentaje: " + owner.getPercentage());
        }
        cbOwners.setItems(ownersInformation);
    }

    private int getOwnerIdFromOwnerInformation(String ownerInformation){
        String[] ownerSplit = ownerInformation.split(" ");
        return Integer.parseInt(ownerSplit[1]);
    }

    private void restartValues(){
        this.txtName.setText("");
        this.txtPercentage.setText("");
        this.rbDelete.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.model = new Model();
        getOwnersInformation();
    }
}

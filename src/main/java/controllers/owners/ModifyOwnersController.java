package controllers.owners;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import models.Model;
import models.interfaces.Animal;

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
    private TextField txtIronBrand;
    @FXML
    private ComboBox<String> cbOwners;
    @FXML
    private RadioButton rbDelete;

    @FXML
    private void modifyOwner(){
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
        if(txtName.getLength() == 0 && txtPercentage.getLength() == 0 && txtIronBrand.getLength() == 0){
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
        if(result.isEmpty()){
            return;
        }
        if (result.get() == ButtonType.OK){
            modifyOwnerNameOrPercentage();
        }
    }

    private void modifyOwnerNameOrPercentage(){
        String selectedOwner = cbOwners.getSelectionModel().getSelectedItem();
        int idSelectedOwner = model.getOwnerIdFromOwnerInformation(selectedOwner);
        String response = "Éxito modificando ";
        if(txtName.getLength() > 0){
            response += "nombre, ";
        }
        if(txtPercentage.getLength() > 0){
            response += "porcentaje, ";
        }
        if(txtIronBrand.getLength() > 0){
            response += "marca hierro, ";
        }
        response = response.substring(0, response.length()-2);
        response += " del dueño";

        boolean status = model.modifyOwner(idSelectedOwner, txtName.getText(), txtPercentage.getText(), txtIronBrand.getText());
        if(!status){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error");
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo modificar el dueño");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(response);
        alert.showAndWait();
        restartValues();
        model.setOwnersInformation(cbOwners);
    }

    private void confirmDeleteOwner(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText("¿Está seguro que desea eliminar el dueño?");

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isEmpty()){
            return;
        }
        if (result.get() == ButtonType.OK){
            deleteOwner();
        }
    }

    private void deleteOwner(){
        String selectedOwner = cbOwners.getSelectionModel().getSelectedItem();
        int idSelected = model.getOwnerIdFromOwnerInformation(selectedOwner);
        ObservableList<Animal> animals = model.getAnimalsByOwnerId(idSelected);
        if(animals == null || animals.size() == 0){
            int status = model.deleteOwner(idSelected);
            if(status == 0){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Éxito");
                alert.setHeaderText("Exito eliminando el dueño");
                alert.showAndWait();
                model.setOwnersInformation(cbOwners);
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

    private void restartValues(){
        this.txtName.setText("");
        this.txtPercentage.setText("");
        this.txtIronBrand.setText("");
        this.rbDelete.setSelected(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        model = new Model();
        model.setOwnersInformation(cbOwners);
    }
}

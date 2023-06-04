package controllers.inventory;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Model;
import models.interfaces.Animal;
import models.interfaces.AnimalState;
import models.interfaces.Weight;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class ShowAnimalWeightsController implements Initializable {

    private final Model model = new Model();
    private final Animal animal;

    @FXML
    private TextArea txtAnimalDescription;
    @FXML
    private TableView<Weight> tblWeights;
    @FXML
    private TableColumn<Weight, String> colNumber;
    @FXML
    private TableColumn<Weight, Double> colWeight;
    @FXML
    private TableColumn<Weight, Date> colDate;
    @FXML
    private Button btnAddWeight;

    public ShowAnimalWeightsController(Animal animal) {
        this.animal = animal;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtAnimalDescription.setText("Información pesos del animal con id: " + animal.getId() + "\n" +
        "Dueño: " + animal.getOwnerInformation() + "\n" + "Número: " + animal.getNumber() + "\n" +
                "Color: " + animal.getColor() + "\n" + "Hierro: " + animal.getIronBrand() + "\n" +
                "Sexo: " + animal.getSex() + "\n" + "Estado: " + animal.getState().toString());

        colNumber.setCellValueFactory(new PropertyValueFactory<>("animalNumber"));
        colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        ObservableList<Weight> weights = model.getAnimalWeights(animal.getId());
        tblWeights.setItems(weights);

        btnAddWeight.setOnAction(event -> {
            try {
                if(animal.getState().toString().equals(AnimalState.death.toString())){
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                    alertError.setTitle("Error");
                    alertError.setHeaderText("El animal se encuentra eliminado");
                    alertError.showAndWait();
                    return;
                } else if(animal.getState().toString().equals(AnimalState.sold.toString())){
                    Alert alertError = new Alert(Alert.AlertType.ERROR, "Error");
                    alertError.setTitle("Error");
                    alertError.setHeaderText("El animal se encuentra vendido");
                    alertError.showAndWait();
                    return;
                }
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/livestock_inventory/inventory/AddAnimalWeight.fxml"));
                AddAnimalWeightController addAnimalWeightController = new AddAnimalWeightController(animal);
                loader.setController(addAnimalWeightController);
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setResizable(false);

                stage.showAndWait();
                if (addAnimalWeightController.getResponse() == 1) {
                    ObservableList<Weight> newWeights = model.getAnimalWeights(animal.getId());
                    tblWeights.setItems(newWeights);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

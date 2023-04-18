package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import models.interfaces.*;

import java.sql.*;
import java.time.LocalDate;

public class Model {

    public ObservableList<Task> getTasksMonth(){
        LocalDate currentDate = LocalDate.now();

        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks WHERE " +
                    "MONTH(assigned_date) = ? ORDER BY assigned_date DESC");
            statement.setInt(1, currentDate.getMonthValue());
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Task> tasks = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateTask stateTask = StateTask.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> stateTask = StateTask.complete;
                    case "canceled" -> stateTask = StateTask.canceled;
                }
                Timestamp creationDateTS = resultSet.getTimestamp("creation_date");
                Date creationDate = new Date(creationDateTS.getTime());
                tasks.add(new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), creationDate,
                        resultSet.getDate("assigned_date"), stateTask));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return tasks;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Owner> getAllOwners(){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM owners ORDER BY active DESC");

            ObservableList<Owner> owners = FXCollections.observableArrayList();

            while (resultSet.next()) {
                owners.add(new Owner(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("percentage"), resultSet.getString("iron_brand"),
                        resultSet.getBoolean("active")));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return owners;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Owner> getActiveOwners(){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM owners WHERE active = true");

            ObservableList<Owner> owners = FXCollections.observableArrayList();

            while (resultSet.next()) {
                owners.add(new Owner(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("percentage"), resultSet.getString("iron_brand"),
                        resultSet.getBoolean("active")));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return owners;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Owner createOwner(String name, double percentage, String ironBrand){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO owners(name, percentage, " +
                            "iron_brand) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setDouble(2, percentage);
            statement.setString(3, ironBrand);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1); // Retorna el id generado
            }
            if(id == -1){
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Owner(id, name, percentage, ironBrand, true);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Animal> getAnimalsByOwnerId(int idOwner){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM animals WHERE " +
                    "id_owner = ? AND state = 'active'");
            statement.setInt(1, idOwner);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateAnimal stateAnimal = StateAnimal.active;
                String state = resultSet.getString("state");
                if(!state.equals("active")){
                    continue;
                }
                animals.add(new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                         resultSet.getString("observations"), stateAnimal));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return animals;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public int deleteOwner(int idOwner) {
        Connection connection = JDBC.connection();
        if(connection == null){
            return -1;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE owners SET active = FALSE " +
                    "WHERE id = ?");
            statement.setInt(1, idOwner);

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if(rowsAffected == 1){
                return 0;
            }else {
                return 1;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
    }
    
    public boolean modifyOwner(int idOwner, String name, String percentage, String ironBrand){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }

        String query = "UPDATE owners SET ";
        if(name.length() > 0){
            query += "name = ?, ";
        }
        if(percentage.length() > 0){
            query += "percentage = ?, ";
        }
        if(ironBrand.length() > 0){
            query += "iron_brand = ?, ";
        }
        query = query.substring(0, query.length()-2);
        query += " WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if(name.length() > 0){
                statement.setString(cont++, name);
            }
            if(percentage.length() > 0){
                statement.setDouble(cont++, Double.parseDouble(percentage));
            }
            if(ironBrand.length() > 0){
                statement.setString(cont++, ironBrand);
            }

            statement.setInt(cont, idOwner);

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getOwnersInformation(ComboBox cbOwners){
        ObservableList<Owner> owners = getActiveOwners();
        ObservableList<String> ownersInformation = FXCollections.observableArrayList();
        for (Owner owner: owners) {
            ownersInformation.add("Id: " + owner.getId() + " | Nombre: " + owner.getName() + " | Porcentaje: "
                    + owner.getPercentage() + " | Marca hierro: " + owner.getIronBrand());
        }
        cbOwners.setItems(ownersInformation);
    }

    public int getOwnerIdFromOwnerInformation(String ownerInformation){
        String[] ownerSplit = ownerInformation.split(" ");
        return Integer.parseInt(ownerSplit[1]);
    }

    public String getOwnerIronBrandFromOwnerInformation(String ownerInformation){
        String[] ownerSplit = ownerInformation.split("Marca hierro: ");
        return ownerSplit[1];
    }

    public Animal createAnimal(int idOwner, String number, int months, String color, double purchaseWeight,
                               String iron_brand, char sex, double purchasePrice, String observations, Date purchaseDate){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO animals(id_owner, number, " +
                            "months, color, purchase_weight, iron_brand, sex, purchase_price, purchase_date, " +
                            "observations, state) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'active')",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, idOwner);
            statement.setString(2, number);
            statement.setInt(3, months);
            statement.setString(4, color);
            statement.setDouble(5, purchaseWeight);
            statement.setString(6, iron_brand);
            statement.setString(7, sex+"");
            statement.setDouble(8, purchasePrice);
            statement.setDate(9, purchaseDate);
            statement.setString(10, observations);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1); // Retorna el id generado
            }
            if(id == -1){
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Animal(id, idOwner, number, months, color, purchaseWeight, iron_brand, sex, purchasePrice,
                    purchaseDate, observations, StateAnimal.active);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Animal> getActiveAnimals(){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT a.id, a.id_owner, o.name, o.percentage, o.active, a.number, a.months, a.color, a.purchase_weight, a.iron_brand, a.sex, a.purchase_price, a.purchase_date, a.observations, a.sale_weight, a.sale_price, a.sale_date, a.state
                    FROM animals a
                    INNER JOIN owners o ON a.id_owner = o.id
                    WHERE a.state = 'active';""");
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateAnimal stateAnimal = StateAnimal.active;
                String state = resultSet.getString("state");
                if(!state.equals("active")){
                    continue;
                }
                Animal animal = new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), stateAnimal);
                animal.setOwnerInformation(new Owner(resultSet.getInt("id_owner"),
                        resultSet.getString("name"), resultSet.getDouble("percentage"),
                        resultSet.getString("iron_brand"), resultSet.getBoolean("active")));
                animals.add(animal);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return animals;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}

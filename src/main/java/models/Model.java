package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
                        resultSet.getDouble("percentage"), resultSet.getBoolean("active")));
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
                        resultSet.getDouble("percentage"), resultSet.getBoolean("active")));
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

    public Owner createOwner(String name, double percentage){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO owners(name, percentage) " +
                    "VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setDouble(2, percentage);
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
            return new Owner(id, name, percentage, true);
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
                    "id_owner = ? AND STATE = 'active'");
            statement.setInt(1, idOwner);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateAnimal stateAnimal = StateAnimal.active;
                String state = resultSet.getString("state");
                if(!state.equals("active")){
                    return null;
                }
                animals.add(new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getInt("months"), resultSet.getString("Color"),
                        resultSet.getDouble("weight"), resultSet.getString("iron_brand"),
                        resultSet.getDate("purchase_date"), resultSet.getString("sex").charAt(0),
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

    public boolean modifyOwnerName(int idOwner, String name) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE owners SET name = ? WHERE id = ?");

            statement.setString(1, name);
            statement.setInt(2, idOwner);

            int rowsAffected = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifyOwnerPercentage(int idOwner, double percentage) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE owners SET percentage = ? WHERE id = ?");

            statement.setDouble(1, percentage);
            statement.setInt(2, idOwner);

            int rowsAffected = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifyOwnerNameAndPercentage(int idOwner, String name, double percentage) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE owners SET percentage = ?, " +
                    "name = ? WHERE id = ?");

            statement.setDouble(1, percentage);
            statement.setString(2, name);
            statement.setInt(3, idOwner);

            int rowsAffected = statement.executeUpdate();

            statement.close();
            connection.close();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

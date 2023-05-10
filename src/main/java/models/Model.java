package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import models.interfaces.*;

import java.sql.*;
import java.time.LocalDate;

public class Model {

    /** ========================================== Tasks ====================================== **/
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

    public ObservableList<Task> getActiveTasks(){

        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks WHERE " +
                    "state = 'active'");
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

    public ObservableList<Task> getAllTasks(){

        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks");
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

    public ObservableList<Task> getFilterTasks(StateTask stateTask, String creationOrAssigned, String dateFrom, String dateTo){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        String query = "SELECT * FROM tasks WHERE ";
        if(stateTask != null){
            query += "state = ? AND ";
        }
        if(creationOrAssigned.length() > 0){
            if(creationOrAssigned.equals("creation")){
                query += "creation_date BETWEEN ? AND ? AND ";
            } else if (creationOrAssigned.equals("assigned")) {
                query += "assigned_date BETWEEN ? AND ? AND ";
            } else {
                return null;
            }
            if(dateFrom.length() < 1 || dateTo.length() < 1){
                return null;
            }
        }

        query = query.substring(0, query.length()-5);

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if(stateTask != null){
                switch (stateTask){
                    case active -> statement.setString(cont++, "active");
                    case canceled -> statement.setString(cont++, "canceled");
                    case complete -> statement.setString(cont++, "complete");
                }
            }
            if(creationOrAssigned.length() > 0){
                statement.setString(cont++, dateFrom);
                statement.setString(cont, dateTo);
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Task> tasks = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateTask stateTaskResultSet = StateTask.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> stateTaskResultSet = StateTask.complete;
                    case "canceled" -> stateTaskResultSet = StateTask.canceled;
                }
                Task task = new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getDate("creation_date"),
                        resultSet.getDate("assigned_date"), stateTaskResultSet);
                tasks.add(task);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return tasks;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean completeTask(Task task) {
        Connection connection = JDBC.connection();
        if(connection == null){
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE tasks SET state = 'complete' " +
                    "WHERE id = ?");
            statement.setInt(1, task.getId());

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            return rowsAffected == 1;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean canceledTask(Task task) {
        Connection connection = JDBC.connection();
        if(connection == null){
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE tasks SET state = 'canceled' " +
                    "WHERE id = ?");
            statement.setInt(1, task.getId());

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            return rowsAffected == 1;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Task createTask(String name, String description, Date assignedDate){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tasks(name, description, " +
                            "assigned_date, state) values(?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDate(3, assignedDate);
            statement.setString(4, "active");
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if(id == -1){
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Task(id, name, description, new Date(new java.util.Date().getTime()), assignedDate, StateTask.active);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    /*  ========================================== ====== ====================================== **/
    /** ========================================== Owners ====================================== **/
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

    public Owner getOwnerById(int id){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM owners WHERE id = ? AND active = TRUE;");
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            Owner owner = null;

            while (resultSet.next()) {
                owner = new Owner(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("percentage"), resultSet.getString("iron_brand"),
                        resultSet.getBoolean("active"));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return owner;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public void setOwnersInformation(ComboBox cbOwners){
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


    /*  ========================================== ====== ====================================== **/
    /** ========================================== Animals ====================================== **/
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

    public boolean sellAnimal(Animal animal, double income, Date date, String descriptionIncome, String saleObservation){
        Connection connection = JDBC.connection();
        if(connection == null){
            return false;
        }
        try {
            PreparedStatement statement;
            if( saleObservation.length() > 0){
                statement = connection.prepareStatement("UPDATE animals SET state = 'sold', observations = ? " +
                        "WHERE id = ?");
                statement.setString(1, saleObservation);
                statement.setInt(2, animal.getId());
            }else{
                statement = connection.prepareStatement("UPDATE animals SET state = 'sold' " +
                        "WHERE id = ?");
                statement.setInt(1, animal.getId());
            }

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if(rowsAffected == 1){
                Finance finance = addIncome(income, date, descriptionIncome);
                return finance != null;
            } else {
                return false;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Animal getActiveAnimalByNumber(String number){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM animals WHERE number = ? AND state = 'active';");
            statement.setString(1, number);
            ResultSet resultSet = statement.executeQuery();

            Animal animal = null;

            while (resultSet.next()) {
                animal = new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), StateAnimal.active);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return animal;
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

    public ObservableList<Animal> getAllAnimals(){
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("""
                    SELECT a.id, a.id_owner, o.name, o.percentage, o.active, a.number, a.months, a.color, a.purchase_weight, a.iron_brand, a.sex, a.purchase_price, a.purchase_date, a.observations, a.sale_weight, a.sale_price, a.sale_date, a.state
                    FROM animals a
                    INNER JOIN owners o ON a.id_owner = o.id;""");
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateAnimal stateAnimal = StateAnimal.active;
                String state = resultSet.getString("state");
                if(!state.equals("active")){
                    switch (state){
                        case "death" -> stateAnimal = StateAnimal.death;
                        case "sold" -> stateAnimal = StateAnimal.sold;
                    }
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

    public ObservableList<Animal> getFilterAnimals(String number, int idOwner, String sex, StateAnimal stateAnimal, String purchaseOrSale, String dateFrom, String dateTo){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        String query = "SELECT a.id, a.id_owner, o.name, o.percentage, o.active, a.number, a.months, a.color, " +
                "a.purchase_weight, a.iron_brand, a.sex, a.purchase_price, a.purchase_date, a.observations, " +
                "a.sale_weight, a.sale_price, a.sale_date, a.state " +
                "FROM animals a " +
                "INNER JOIN owners o ON a.id_owner = o.id " +
                "WHERE ";
        if(number.length() > 0){
            query += "a.number like ? AND ";
        }
        if(idOwner > 0){
            query += "a.id_owner = ? AND ";
        }
        if(sex.length() > 0){
            query += "a.sex = ? AND ";
        }
        if(stateAnimal != null){
            query += "a.state = ? AND ";
        }
        if(purchaseOrSale.length() > 0){
            if(purchaseOrSale.equals("purchase")){
                query += "a.purchase_date BETWEEN ? AND ? AND ";
            } else if (purchaseOrSale.equals("sale")) {
                query += "a.sale_date BETWEEN ? AND ? AND ";
            } else {
                return null;
            }
            if(dateFrom.length() < 1 || dateTo.length() < 1){
                return null;
            }
        }

        query = query.substring(0, query.length()-5);

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if(number.length() > 0){
                statement.setString(cont++, "%" + number + "%");
            }
            if(idOwner > 0){
                statement.setInt(cont++, idOwner);
            }
            if(sex.length() > 0){
                statement.setString(cont++, sex);
            }
            if(stateAnimal != null){
                switch (stateAnimal){
                    case active -> statement.setString(cont++, "active");
                    case sold -> statement.setString(cont++, "sold");
                    case death -> statement.setString(cont++, "death");
                }
            }
            if(purchaseOrSale.length() > 0){
                statement.setString(cont++, dateFrom);
                statement.setString(cont, dateTo);
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                StateAnimal stateAnimalResultSet = StateAnimal.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "death" -> stateAnimalResultSet = StateAnimal.death;
                    case "sold" -> stateAnimalResultSet = StateAnimal.sold;
                }
                Animal animal = new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), stateAnimalResultSet);
                animal.setOwnerInformation(new Owner(resultSet.getInt("id_owner"),
                        resultSet.getString("name"), resultSet.getDouble("percentage"),
                        resultSet.getString("iron_brand"), resultSet.getBoolean("active")));
                animals.add(animal);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return animals;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteAnimal(Animal animal){
        Connection connection = JDBC.connection();
        if(connection == null){
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE animals SET state = 'death' " +
                    "WHERE id = ?");
            statement.setInt(1, animal.getId());

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            return rowsAffected == 1;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /*  ========================================== ====== ====================================== **/
    /** ========================================== Finances ====================================== **/
    public Finance addExpense(double expense, Date purchaseDate, String description) {
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO finances(date, expense, " +
                    "description) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, purchaseDate);
            statement.setDouble(2, expense);
            statement.setString(3, description);
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
            return new Finance(id, purchaseDate, 0, expense, description);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Finance addIncome(double income, Date purchaseDate, String description) {
        Connection connection = JDBC.connection();
        if(connection == null){
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO finances(date, income, " +
                    "description) VALUES(?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, purchaseDate);
            statement.setDouble(2, income);
            statement.setString(3, description);
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
            return new Finance(id, purchaseDate, income, 0, description);
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}

package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import models.interfaces.*;

import java.sql.*;
import java.time.LocalDate;

public class Model {

    /**
     * ========================================== Tasks ======================================
     **/
    public ObservableList<Task> getActiveTasksMonth() {
        LocalDate currentDate = LocalDate.now();

        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks WHERE " +
                    "MONTH(assigned_date) = ? AND state = 'active' ORDER BY assigned_date DESC");
            statement.setInt(1, currentDate.getMonthValue());
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Task> tasks = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventState = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventState = EventState.complete;
                    case "canceled" -> eventState = EventState.canceled;
                }
                Timestamp creationDateTS = resultSet.getTimestamp("creation_date");
                Date creationDate = new Date(creationDateTS.getTime());
                tasks.add(new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), creationDate,
                        resultSet.getDate("assigned_date"), eventState));
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

    public ObservableList<Task> getActiveTasks() {

        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks WHERE " +
                    "state = 'active'");
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Task> tasks = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventState = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventState = EventState.complete;
                    case "canceled" -> eventState = EventState.canceled;
                }
                Timestamp creationDateTS = resultSet.getTimestamp("creation_date");
                Date creationDate = new Date(creationDateTS.getTime());
                tasks.add(new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), creationDate,
                        resultSet.getDate("assigned_date"), eventState));
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

    public ObservableList<Task> getAllTasks() {

        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tasks");
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Task> tasks = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventState = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventState = EventState.complete;
                    case "canceled" -> eventState = EventState.canceled;
                }
                Timestamp creationDateTS = resultSet.getTimestamp("creation_date");
                Date creationDate = new Date(creationDateTS.getTime());
                tasks.add(new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), creationDate,
                        resultSet.getDate("assigned_date"), eventState));
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

    public ObservableList<Task> getFilterTasks(EventState eventState, String creationOrAssigned, String dateFrom, String dateTo) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        String query = "SELECT * FROM tasks WHERE ";
        if (eventState != null) {
            query += "state = ? AND ";
        }
        if (creationOrAssigned.length() > 0) {
            if (creationOrAssigned.equals("creation")) {
                query += "creation_date BETWEEN ? AND ? AND ";
            } else if (creationOrAssigned.equals("assigned")) {
                query += "assigned_date BETWEEN ? AND ? AND ";
            } else {
                return null;
            }
            if (dateFrom.length() < 1 || dateTo.length() < 1) {
                return null;
            }
        }

        query = query.substring(0, query.length() - 5);

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if (eventState != null) {
                switch (eventState) {
                    case active -> statement.setString(cont++, "active");
                    case canceled -> statement.setString(cont++, "canceled");
                    case complete -> statement.setString(cont++, "complete");
                }
            }
            if (creationOrAssigned.length() > 0) {
                statement.setString(cont++, dateFrom);
                statement.setString(cont, dateTo);
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Task> tasks = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventStateResultSet = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventStateResultSet = EventState.complete;
                    case "canceled" -> eventStateResultSet = EventState.canceled;
                }
                Task task = new Task(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getString("description"), resultSet.getDate("creation_date"),
                        resultSet.getDate("assigned_date"), eventStateResultSet);
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
        if (connection == null) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelTask(Task task) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Task createTask(String name, String description, Date assignedDate) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Task(id, name, description, new Date(new java.util.Date().getTime()), assignedDate, EventState.active);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Task createTask(String name, String description, Date assignedDate, int idVeterinaryAssistance) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tasks(name, description, " +
                            "assigned_date, state, id_veterinary_assistance) values(?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setDate(3, assignedDate);
            statement.setString(4, "active");
            statement.setInt(5, idVeterinaryAssistance);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Task(id, name, description, new Date(new java.util.Date().getTime()), assignedDate, EventState.active);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * ========================================== Owners ======================================
     **/
    public ObservableList<Owner> getActiveOwners() {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Owner createOwner(String name, double percentage, String ironBrand) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Owner(id, name, percentage, ironBrand, true);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int deleteOwner(int idOwner) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return -1;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE owners SET active = FALSE " +
                    "WHERE id = ?");
            statement.setInt(1, idOwner);

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if (rowsAffected == 1) {
                return 0;
            } else {
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public boolean modifyOwner(int idOwner, String name, String percentage, String ironBrand) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }

        String query = "UPDATE owners SET ";
        if (name.length() > 0) {
            query += "name = ?, ";
        }
        if (percentage.length() > 0) {
            query += "percentage = ?, ";
        }
        if (ironBrand.length() > 0) {
            query += "iron_brand = ?, ";
        }
        query = query.substring(0, query.length() - 2);
        query += " WHERE id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if (name.length() > 0) {
                statement.setString(cont++, name);
            }
            if (percentage.length() > 0) {
                statement.setDouble(cont++, Double.parseDouble(percentage));
            }
            if (ironBrand.length() > 0) {
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

    public Owner getOwnerById(int id) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setOwnersInformation(ComboBox cbOwners) {
        ObservableList<Owner> owners = getActiveOwners();
        ObservableList<String> ownersInformation = FXCollections.observableArrayList();
        for (Owner owner : owners) {
            ownersInformation.add("Id: " + owner.getId() + " | Nombre: " + owner.getName() + " | Porcentaje: "
                    + owner.getPercentage() + " | Marca hierro: " + owner.getIronBrand());
        }
        cbOwners.setItems(ownersInformation);
    }

    public int getOwnerIdFromOwnerInformation(String ownerInformation) {
        String[] ownerSplit = ownerInformation.split(" ");
        return Integer.parseInt(ownerSplit[1]);
    }

    public String getOwnerIronBrandFromOwnerInformation(String ownerInformation) {
        String[] ownerSplit = ownerInformation.split("Marca hierro: ");
        return ownerSplit[1];
    }

    public String getOwnerNameFromOwnerInformation(String ownerInformation) {
        String[] ownerSplit = ownerInformation.split("Nombre: ");
        ownerSplit = ownerSplit[1].split(" \\| Porcentaje");
        return ownerSplit[0];
    }


    /**
     * ========================================== Animals ======================================
     **/
    public ObservableList<Animal> getAnimalsByOwnerId(int idOwner) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM animals WHERE " +
                    "id_owner = ? AND state = 'active'");
            statement.setInt(1, idOwner);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                AnimalState animalState = AnimalState.active;
                String state = resultSet.getString("state");
                if (!state.equals("active")) {
                    continue;
                }
                animals.add(new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), animalState,
                        resultSet.getDouble("sale_price"), resultSet.getDate("sale_date"),
                        resultSet.getDouble("sale_weight")));
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

    public Animal createAnimal(int idOwner, String number, int months, String color, double purchaseWeight,
                               String iron_brand, char sex, double purchasePrice, String observations, Date purchaseDate) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
            statement.setString(7, sex + "");
            statement.setDouble(8, purchasePrice);
            statement.setDate(9, purchaseDate);
            statement.setString(10, observations);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            addAnimalWeight(id, number, purchaseWeight, purchaseDate);
            return new Animal(id, idOwner, number, months, color, purchaseWeight, iron_brand, sex, purchasePrice,
                    purchaseDate, observations, AnimalState.active, 0, null, 0);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean sellAnimal(Animal animal, double income, Date date, String descriptionIncome, String saleObservation) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement;
            if (saleObservation.length() > 0) {
                statement = connection.prepareStatement("UPDATE animals SET state = 'sold', observations = ?, " +
                        "sale_weight = ?, sale_price = ?, sale_date = ? WHERE id = ?");
                statement.setString(1, saleObservation);
                statement.setInt(2, animal.getId());
            } else {
                statement = connection.prepareStatement("UPDATE animals SET state = 'sold', sale_weight = ?, " +
                        "sale_price = ?, sale_date = ? WHERE id = ?");
                statement.setInt(1, animal.getId());
            }

            int cont = 1;
            if(saleObservation.length() > 0){
                statement.setString(cont++, saleObservation);
            }
            statement.setDouble(cont++, animal.getSaleWeight());
            statement.setDouble(cont++, animal.getSalePrice());
            statement.setDate(cont++, animal.getSaleDate());
            statement.setInt(cont, animal.getId());

            int rowsAffected = statement.executeUpdate();
            statement.close();
            connection.close();
            if (rowsAffected == 1) {
                addIncome(income, date, descriptionIncome);
                addAnimalWeight(animal.getId(), animal.getNumber(), animal.getSaleWeight(), animal.getSaleDate());
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Animal getActiveAnimalByNumber(String number) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
                        resultSet.getString("observations"), AnimalState.active,
                        resultSet.getDouble("sale_price"), resultSet.getDate("sale_date"),
                        resultSet.getDouble("sale_weight"));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return animal;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Animal> getActiveAnimals() {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
                AnimalState animalState = AnimalState.active;
                String state = resultSet.getString("state");
                if (!state.equals("active")) {
                    continue;
                }
                Animal animal = new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), animalState,
                        resultSet.getDouble("sale_price"), resultSet.getDate("sale_date"),
                        resultSet.getDouble("sale_weight"));
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

    public ObservableList<Animal> getAllAnimals() {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
                AnimalState animalState = AnimalState.active;
                String state = resultSet.getString("state");
                if (!state.equals("active")) {
                    switch (state) {
                        case "death" -> animalState = AnimalState.death;
                        case "sold" -> animalState = AnimalState.sold;
                    }
                }
                Animal animal = new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), animalState,
                        resultSet.getDouble("sale_price"), resultSet.getDate("sale_date"),
                        resultSet.getDouble("sale_weight"));
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

    public ObservableList<Animal> getFilterAnimals(String number, int idOwner, String sex, AnimalState animalState, String purchaseOrSale, String dateFrom, String dateTo) {
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
        if (number.length() > 0) {
            query += "a.number like ? AND ";
        }
        if (idOwner > 0) {
            query += "a.id_owner = ? AND ";
        }
        if (sex.length() > 0) {
            query += "a.sex = ? AND ";
        }
        if (animalState != null) {
            query += "a.state = ? AND ";
        }
        if (purchaseOrSale.length() > 0) {
            if (purchaseOrSale.equals("purchase")) {
                query += "a.purchase_date BETWEEN ? AND ? AND ";
            } else if (purchaseOrSale.equals("sale")) {
                query += "a.sale_date BETWEEN ? AND ? AND ";
            } else {
                return null;
            }
            if (dateFrom.length() < 1 || dateTo.length() < 1) {
                return null;
            }
        }

        query = query.substring(0, query.length() - 5);

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if (number.length() > 0) {
                statement.setString(cont++, "%" + number + "%");
            }
            if (idOwner > 0) {
                statement.setInt(cont++, idOwner);
            }
            if (sex.length() > 0) {
                statement.setString(cont++, sex);
            }
            if (animalState != null) {
                switch (animalState) {
                    case active -> statement.setString(cont++, "active");
                    case sold -> statement.setString(cont++, "sold");
                    case death -> statement.setString(cont++, "death");
                }
            }
            if (purchaseOrSale.length() > 0) {
                statement.setString(cont++, dateFrom);
                statement.setString(cont, dateTo);
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Animal> animals = FXCollections.observableArrayList();

            while (resultSet.next()) {
                AnimalState animalStateResultSet = AnimalState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "death" -> animalStateResultSet = AnimalState.death;
                    case "sold" -> animalStateResultSet = AnimalState.sold;
                }
                Animal animal = new Animal(resultSet.getInt("id"), resultSet.getInt("id_owner"),
                        resultSet.getString("number"), resultSet.getInt("months"),
                        resultSet.getString("color"), resultSet.getDouble("purchase_weight"),
                        resultSet.getString("iron_brand"), resultSet.getString("sex").charAt(0),
                        resultSet.getDouble("purchase_price"), resultSet.getDate("purchase_date"),
                        resultSet.getString("observations"), animalStateResultSet,
                        resultSet.getDouble("sale_price"), resultSet.getDate("sale_date"),
                        resultSet.getDouble("sale_weight"));
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

    public boolean deleteAnimal(Animal animal) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ========================================== Weight ======================================
     **/

    public boolean addAnimalWeight(int idAnimal, String number, double weight, Date date){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO weights(id_animal, " +
                    "animal_number, weight, date) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, idAnimal);
            statement.setString(2, number);
            statement.setDouble(3, weight);
            statement.setDate(4, date);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return false;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ObservableList<Weight> getAnimalWeights(int idAnimal){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM weights WHERE id_animal " +
                    "= ? ORDER BY date ASC");
            statement.setInt(1, idAnimal);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<Weight> weights = FXCollections.observableArrayList();

            while (resultSet.next()) {
                weights.add(new Weight(resultSet.getInt("id"), resultSet.getInt("id_animal"),
                        resultSet.getString("animal_number"), resultSet.getDouble("weight"),
                        resultSet.getDate("date")));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return weights;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * ========================================== Finances ======================================
     **/
    public Finance addExpense(double expense, Date purchaseDate, String description) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Finance(id, purchaseDate, 0, expense, description);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Finance addIncome(double income, Date purchaseDate, String description) {
        Connection connection = JDBC.connection();
        if (connection == null) {
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
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new Finance(id, purchaseDate, income, 0, description);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Finance> getFinances() {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM finances");

            ObservableList<Finance> finances = FXCollections.observableArrayList();

            while (resultSet.next()) {
                finances.add(new Finance(resultSet.getInt("id"), resultSet.getDate("date"),
                        resultSet.getDouble("income"), resultSet.getDouble("expense"),
                        resultSet.getString("description")));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return finances;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<Finance> getFilterFinances(String financesType, String dateFrom, String dateTo) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        if (financesType.length() == 0 && (dateFrom.length() == 0 || dateTo.length() == 0)) {
            return null;
        }
        String query = "SELECT * FROM finances WHERE ";
        if (financesType.length() > 0) {
            if (financesType.equalsIgnoreCase("income")) {
                query += "income > 0 AND ";
            } else if (financesType.equalsIgnoreCase("expense")) {
                query += "expense > 0 AND ";
            }
        }
        if (dateFrom.length() > 0 && dateTo.length() > 0) {
            query += "date BETWEEN ? AND ? AND ";
        }
        query = query.substring(0, query.length() - 5);
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            if (dateFrom.length() > 0 && dateTo.length() > 0) {
                statement.setString(1, dateFrom);
                statement.setString(2, dateTo);
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<Finance> finances = FXCollections.observableArrayList();

            while (resultSet.next()) {
                Finance finance = new Finance(resultSet.getInt("id"), resultSet.getDate("date"),
                        resultSet.getDouble("income"), resultSet.getDouble("expense"),
                        resultSet.getString("description"));
                finances.add(finance);
            }
            resultSet.close();
            statement.close();
            connection.close();
            return finances;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * ========================================== Veterinary Assistance ======================================
     **/

    public VeterinaryAssistance createCompletedVeterinaryAssistance(String name, Date completionDate, Double cost,
                                                                    String description, Date nextDate){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO veterinary_assistance(completion_date, " +
                    "name, description, cost, next_date, state) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, completionDate);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setDouble(4, cost);
            statement.setDate(5, nextDate);
            statement.setString(6, "complete");
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new VeterinaryAssistance(id, null, completionDate, name, description, cost, nextDate, EventState.complete);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public VeterinaryAssistance createAssignedVeterinaryAssistance(String name, Date assignedDate, String description){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO veterinary_assistance(assigned_date, " +
                    "name, description, state) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setDate(1, assignedDate);
            statement.setString(2, name);
            statement.setString(3, description);
            statement.setString(4, "active");
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            int id = -1;
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
            if (id == -1) {
                return null;
            }
            resultSet.close();
            statement.close();
            connection.close();
            return new VeterinaryAssistance(id, assignedDate, null, name, description, 0, null, EventState.active);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<VeterinaryAssistance> getActiveVeterinaryAssistance() {

        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM veterinary_assistance WHERE " +
                    "state = 'active'");
            ResultSet resultSet = statement.executeQuery();

            ObservableList<VeterinaryAssistance> veterinaryAssistance = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventState = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventState = EventState.complete;
                    case "canceled" -> eventState = EventState.canceled;
                }
                veterinaryAssistance.add(new VeterinaryAssistance(resultSet.getInt("id"),
                        resultSet.getDate("assigned_date"), resultSet.getDate("completion_date"),
                        resultSet.getString("name"), resultSet.getString("description"),
                        resultSet.getDouble("cost"), resultSet.getDate("next_date"),
                        eventState));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return veterinaryAssistance;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<VeterinaryAssistance> getAllVeterinaryAssistance() {

        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM veterinary_assistance");
            ResultSet resultSet = statement.executeQuery();

            ObservableList<VeterinaryAssistance> veterinaryAssistance = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventState = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventState = EventState.complete;
                    case "canceled" -> eventState = EventState.canceled;
                }
                veterinaryAssistance.add(new VeterinaryAssistance(resultSet.getInt("id"),
                        resultSet.getDate("assigned_date"), resultSet.getDate("completion_date"),
                        resultSet.getString("name"), resultSet.getString("description"),
                        resultSet.getDouble("cost"), resultSet.getDate("next_date"),
                        eventState));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return veterinaryAssistance;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ObservableList<VeterinaryAssistance> getFilterVeterinaryAssistance(EventState eventState, String dateFilterType, String dateFrom, String dateTo) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return null;
        }
        String query = "SELECT * FROM veterinary_assistance WHERE ";
        if (eventState != null) {
            query += "state = ? AND ";
        }
        if (dateFilterType.length() > 0) {
            switch (dateFilterType) {
                case "completion" -> query += "completion_date BETWEEN ? AND ? AND ";
                case "assigned" -> query += "assigned_date BETWEEN ? AND ? AND ";
                case "next" -> query += "next_date BETWEEN ? AND ? AND ";
                default -> {
                    return null;
                }
            }
            if (dateFrom.length() < 1 || dateTo.length() < 1) {
                return null;
            }
        }

        query = query.substring(0, query.length() - 5);

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if (eventState != null) {
                switch (eventState) {
                    case active -> statement.setString(cont++, "active");
                    case canceled -> statement.setString(cont++, "canceled");
                    case complete -> statement.setString(cont++, "complete");
                }
            }
            if (dateFilterType.length() > 0) {
                statement.setString(cont++, dateFrom);
                statement.setString(cont, dateTo);
            }

            ResultSet resultSet = statement.executeQuery();

            ObservableList<VeterinaryAssistance> veterinaryAssistance = FXCollections.observableArrayList();

            while (resultSet.next()) {
                EventState eventStateResultSet = EventState.active;
                String state = resultSet.getString("state");
                switch (state) {
                    case "complete" -> eventStateResultSet = EventState.complete;
                    case "canceled" -> eventStateResultSet = EventState.canceled;
                }
                veterinaryAssistance.add(new VeterinaryAssistance(resultSet.getInt("id"),
                        resultSet.getDate("assigned_date"), resultSet.getDate("completion_date"),
                        resultSet.getString("name"), resultSet.getString("description"),
                        resultSet.getDouble("cost"), resultSet.getDate("next_date"),
                        eventStateResultSet));
            }
            resultSet.close();
            statement.close();
            connection.close();
            return veterinaryAssistance;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean completeVeterinaryAssistance(VeterinaryAssistance veterinaryAssistance, boolean modifyDescription,
                                                boolean modifyCost) {
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
            String query = "UPDATE veterinary_assistance SET state = " +
                    "'complete', ";
            if(modifyDescription){
                query += "description = ?, ";
            }
            if(modifyCost){
                query += "cost = ?, ";
            }
            query = query.substring(0, query.length()-2);
            query += " WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            int cont = 1;
            if(modifyDescription){
                statement.setString(cont++, veterinaryAssistance.getDescription());
            }
            if(modifyCost){
                statement.setDouble(cont++, veterinaryAssistance.getCost());
            }
            statement.setInt(cont, veterinaryAssistance.getId());
            int rowsAffected = statement.executeUpdate();
            PreparedStatement statementTask = connection.prepareStatement("UPDATE tasks SET state = 'complete' WHERE " +
                    "id_veterinary_assistance = ?");
            statementTask.setInt(1, veterinaryAssistance.getId());
            statementTask.executeUpdate();
            statementTask.close();
            statement.close();
            connection.close();
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelVeterinaryAssistance(VeterinaryAssistance veterinaryAssistance){
        Connection connection = JDBC.connection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE veterinary_assistance SET state =" +
                    " 'canceled' WHERE id = ?");
            statement.setInt(1, veterinaryAssistance.getId());
            PreparedStatement statementTask = connection.prepareStatement("UPDATE tasks SET state = 'canceled' " +
                    "WHERE id_veterinary_assistance = ?");
            statementTask.setInt(1, veterinaryAssistance.getId());
            statementTask.executeUpdate();
            int rowsAffected = statement.executeUpdate();
            statement.close();
            statementTask.close();
            connection.close();
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

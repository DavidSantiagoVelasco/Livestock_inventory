package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.interfaces.StateTask;
import models.interfaces.Task;

import java.sql.*;
import java.time.LocalDate;

public class TasksModel {

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
            return tasks;
        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}

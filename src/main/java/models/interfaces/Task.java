package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;

public class Task {

    private String name;
    private String description;
    private Timestamp creationDate;
    private Date assignedDate;
    private String state;

    public Task(){
    }

    public void getTasks(){
        Connection connection = JDBC.Connection();
        if(connection == null){
            return;
        }

    }
}

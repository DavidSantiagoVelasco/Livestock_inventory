package models.interfaces;

import java.sql.Date;
import java.sql.Timestamp;

public class Task {

    private final int id;
    private final String name;
    private final String description;
    private final Timestamp creationDate;
    private Date assignedDate;
    private StateTask state;

    public Task(int id, String name, String description, Timestamp creationDate, Date assignedDate, StateTask state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.assignedDate = assignedDate;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public String getState() {
        switch (this.state){
            case active:
                return "active";
            case complete:
                return "complete";
            case canceled:
                return "canceled";
            default:
                return "";
        }
    }

    public void setState(StateTask state) {
        this.state = state;
    }

    public void setAssignedDate(Date date){
        this.assignedDate = date;
    }
}

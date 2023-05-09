package models.interfaces;

import java.sql.Date;

public class Task {

    private final int id;
    private final String name;
    private final String description;
    private final Date creationDate;
    private Date assignedDate;
    private StateTask state;

    public Task(int id, String name, String description, Date creationDate, Date assignedDate, StateTask state) {
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

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public StateTask getState() {
        return state;
    }

    public void setState(StateTask state) {
        this.state = state;
    }

    public void setAssignedDate(Date date){
        this.assignedDate = date;
    }
}

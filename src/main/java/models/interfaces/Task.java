package models.interfaces;

import java.sql.Date;

public class Task {

    private final int id;
    private final String name;
    private final String description;
    private final Date creationDate;
    private Date assignedDate;
    private EventState state;
    private final int idVeterinaryAssistance;

    public Task(int id, String name, String description, Date creationDate, Date assignedDate, EventState state, int idVeterinaryAssistance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.assignedDate = assignedDate;
        this.state = state;
        this.idVeterinaryAssistance = idVeterinaryAssistance;
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

    public EventState getState() {
        return state;
    }

    public int getIdVeterinaryAssistance() {
        return idVeterinaryAssistance;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public void setAssignedDate(Date date) {
        this.assignedDate = date;
    }
}

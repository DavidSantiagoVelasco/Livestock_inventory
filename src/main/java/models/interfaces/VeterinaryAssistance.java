package models.interfaces;

import java.sql.Date;

public class VeterinaryAssistance {

    private final int id;
    private final Date assignedDate;
    private final Date completionDate;
    private final String name;
    private final String description;
    private final double cost;
    private final Date nextDate;

    private final EventState state;

    public VeterinaryAssistance(int id, Date assignedDate, Date completionDate, String name, String description,
                                double cost, Date nextDate, EventState state) {
        this.id = id;
        this.assignedDate = assignedDate;
        this.completionDate = completionDate;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.nextDate = nextDate;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getCost() {
        return cost;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public EventState getState() {
        return state;
    }
}

package models.interfaces;

import java.sql.Date;

public class VeterinaryAssistance {

    private final int id;
    private final Date assigned_date;
    private final Date completion_date;
    private final String name;
    private final String description;
    private final double cost;
    private final Date next_date;

    public VeterinaryAssistance(int id, Date assigned_date, Date completion_date, String name, String description,
                                double cost, Date next_date) {
        this.id = id;
        this.assigned_date = assigned_date;
        this.completion_date = completion_date;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.next_date = next_date;
    }

    public int getId() {
        return id;
    }

    public Date getAssigned_date() {
        return assigned_date;
    }

    public Date getCompletion_date() {
        return completion_date;
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

    public Date getNext_date() {
        return next_date;
    }
}

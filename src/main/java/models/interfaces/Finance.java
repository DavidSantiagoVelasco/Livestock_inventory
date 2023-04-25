package models.interfaces;

import java.sql.Date;

public class Finance {

    private final int id;
    private final Date date;
    private double income;
    private double expense;
    private String description;

    public Finance(int id, Date date, double income, double expense, String description) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.expense = expense;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public double getIncome() {
        return income;
    }

    public double getExpense() {
        return expense;
    }

    public String getDescription() {
        return description;
    }
}

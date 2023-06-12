package models.interfaces;

import java.sql.Date;

public record Finance(int id, Date date, double income, double expense, String description) {

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

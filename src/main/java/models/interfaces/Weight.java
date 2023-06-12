package models.interfaces;

import java.sql.Date;

public record Weight(int id, int idAnimal, String animalNumber, double weight, Date date) {

    public int getId() {
        return id;
    }

    public int getIdAnimal() {
        return idAnimal;
    }

    public String getAnimalNumber() {
        return animalNumber;
    }

    public double getWeight() {
        return weight;
    }

    public Date getDate() {
        return date;
    }

}

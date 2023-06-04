package models.interfaces;

import java.sql.Date;

public class Weight {
    private final int id;
    private final int idAnimal;
    private final String animalNumber;
    private final double weight;
    private final Date date;

    public Weight(int id, int idAnimal, String animalNumber, double weight, Date date) {
        this.id = id;
        this.idAnimal = idAnimal;
        this.animalNumber = animalNumber;
        this.weight = weight;
        this.date = date;
    }

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

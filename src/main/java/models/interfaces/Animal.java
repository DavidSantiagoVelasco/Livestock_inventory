package models.interfaces;

import java.sql.Date;

public class Animal {

    private final int id;
    private int idOwner;
    private int months;
    private String color;
    private double weight;
    private String ironBrand;
    private Date purchaseDate;
    private char sex;
    private String observations;
    private StateAnimal state;


    public Animal(int id, int idOwner, int months, String color, double weight, String ironBrand, Date purchaseDate, char sex, String observations, StateAnimal state) {
        this.id = id;
        this.idOwner = idOwner;
        this.months = months;
        this.color = color;
        this.weight = weight;
        this.ironBrand = ironBrand;
        this.purchaseDate = purchaseDate;
        this.sex = sex;
        this.observations = observations;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public String getColor() {
        return color;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getIronBrand() {
        return ironBrand;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public char getSex() {
        return sex;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public StateAnimal getState() {
        return state;
    }

    public void setState(StateAnimal state) {
        this.state = state;
    }
}

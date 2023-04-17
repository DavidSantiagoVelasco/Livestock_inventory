package models.interfaces;

import java.sql.Date;

public class Animal {

    private final int id;
    private int idOwner;
    private String number;
    private int months;
    private String color;
    private double purchaseWeight;
    private String ironBrand;
    private char sex;
    private double purchasePrice;
    private Date purchaseDate;
    private String observations;
    private double saleWeight;
    private double salePrice;
    private Date saleDate;
    private StateAnimal state;

    public Animal(int id, int idOwner, String number, int months, String color, double purchaseWeight, String ironBrand, char sex, double purchasePrice, Date purchaseDate, String observations, StateAnimal state) {
        this.id = id;
        this.idOwner = idOwner;
        this.number = number;
        this.months = months;
        this.color = color;
        this.purchaseWeight = purchaseWeight;
        this.ironBrand = ironBrand;
        this.sex = sex;
        this.purchasePrice = purchasePrice;
        this.purchaseDate = purchaseDate;
        this.observations = observations;
        this.state = state;
    }
}

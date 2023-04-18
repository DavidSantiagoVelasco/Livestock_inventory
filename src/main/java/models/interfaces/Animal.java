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

    private Owner ownerInformation;

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

    public int getId() {
        return id;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public String getNumber() {
        return number;
    }

    public int getMonths() {
        return months;
    }

    public String getColor() {
        return color;
    }

    public double getPurchaseWeight() {
        return purchaseWeight;
    }

    public String getIronBrand() {
        return ironBrand;
    }

    public char getSex() {
        return sex;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public String getObservations() {
        return observations;
    }

    public double getSaleWeight() {
        return saleWeight;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public StateAnimal getState() {
        return state;
    }

    public void setOwnerInformation(Owner ownerInformation){
        this.ownerInformation = ownerInformation;
    }

    public String getOwnerInformation(){
        return "Id: " + ownerInformation.getId() + " | Nombre: " + ownerInformation.getName();
    }

    public Owner getOwner(){
        return ownerInformation;
    }
}

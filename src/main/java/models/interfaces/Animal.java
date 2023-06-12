package models.interfaces;

import java.sql.Date;

public class Animal {

    private final int id;
    private final int idOwner;
    private final String number;
    private final int months;
    private final String color;
    private final double purchaseWeight;
    private final String ironBrand;
    private final char sex;
    private final double purchasePrice;
    private final Date purchaseDate;
    private final String observations;
    private double saleWeight;
    private double salePrice;
    private Date saleDate;
    private final AnimalState state;

    private Owner ownerInformation;

    public Animal(int id, int idOwner, String number, int months, String color, double purchaseWeight, String ironBrand, char sex, double purchasePrice, Date purchaseDate, String observations, AnimalState state,
                  double salePrice, Date saleDate, double saleWeight) {
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
        this.salePrice = salePrice;
        this.saleWeight = saleWeight;
        this.saleDate = saleDate;
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

    public AnimalState getState() {
        return state;
    }

    public String getOwnerInformation(){
        return "Id: " + ownerInformation.getId() + " | Nombre: " + ownerInformation.getName();
    }

    public Owner getOwner(){
        return ownerInformation;
    }

    public void setOwnerInformation(Owner ownerInformation){
        this.ownerInformation = ownerInformation;
    }

    public void setSaleWeight(double saleWeight) {
        this.saleWeight = saleWeight;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }
}

package models.interfaces;

public class Owner {

    private final int id;
    private String name;
    private double percentage;
    private String ironBrand;
    private boolean active;

    public Owner(int id, String name, double percentage, String ironBrand, boolean active) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
        this.ironBrand = ironBrand;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getIronBrand() {
        return ironBrand;
    }

    public void setIronBrand(String ironBrand) {
        this.ironBrand = ironBrand;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

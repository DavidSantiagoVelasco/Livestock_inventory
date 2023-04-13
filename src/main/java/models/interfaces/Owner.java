package models.interfaces;

public class Owner {

    private final int id;
    private String name;
    private double percentage;

    private boolean active;

    public Owner(int id, String name, double percentage, boolean active) {
        this.id = id;
        this.name = name;
        this.percentage = percentage;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

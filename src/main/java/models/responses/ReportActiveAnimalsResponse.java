package models.responses;

public class ReportActiveAnimalsResponse {

    private final String name;
    private final int males;
    private final int females;

    public ReportActiveAnimalsResponse(String name, int males, int females) {
        this.name = name;
        this.males = males;
        this.females = females;
    }

    public String getName() {
        return name;
    }

    public int getMales() {
        return males;
    }

    public int getFemales() {
        return females;
    }

    public int getTotalAnimals(){
        return males + females;
    }
}

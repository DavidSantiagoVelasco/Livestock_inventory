package models.responses;

public record ReportActiveAnimalsResponse(String name, int males, int females) {

    public int getTotalAnimals() {
        return males + females;
    }
}

package models.interfaces;

public enum StateAnimal {
    active,
    sold,
    death;


    @Override
    public String toString() {
        return switch (this) {
            case active -> "Activo";
            case sold -> "Vendido";
            case death -> "Muerto";
            default -> "";
        };
    }
}

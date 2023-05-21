package models.interfaces;

public enum EventState {
    active,
    complete,
    canceled;

    @Override
    public String toString() {
        return switch (this) {
            case active -> "Activo";
            case complete -> "Completado";
            case canceled -> "Cancelado";
        };
    }
}

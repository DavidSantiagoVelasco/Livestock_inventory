package models.interfaces;

public enum StateTask {
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

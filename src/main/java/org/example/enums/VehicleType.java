package org.example.enums;

/**
 * Enum class of vehicle's types
 */
public enum VehicleType {
    PLANE("PLANE"),
    BOAT("BOAT"),
    SHIP("SHIP"),
    BICYCLE("BICYCLE"),
    SPACESHIP("SPACESHIP");

    private final String typeName;

    VehicleType(String description) {
        this.typeName = description;
    }

    public String getTypeName() {
        return typeName;
    }
}

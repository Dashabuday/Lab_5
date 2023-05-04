package org.example.enums;

/**
 * Enum class of fuel's types
 */
public enum FuelType implements Comparable<FuelType> {
    GASOLINE("GASOLINE"),
    KEROSENE("KEROSENE"),
    MANPOWER("MANPOWER"),
    PLASMA("PLASMA");

    private final String typeName;

    FuelType(String description) {
        this.typeName = description;
    }

    public String getTypeName() {
        return typeName;
    }
}

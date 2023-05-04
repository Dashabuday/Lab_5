package org.example.models;

import org.example.enums.FuelType;
import org.example.enums.VehicleType;

import java.time.LocalDate;

/**
 * Class for validating and storing vehicle's parameters
 */
public class Vehicle implements Comparable<Vehicle> {
    private static Integer nextId;
    private final Integer id;
    private String name;
    private Coordinates coordinates;
    private final LocalDate creationDate;
    private int enginePower;
    private VehicleType type;
    private FuelType fuelType;

    public Vehicle(
            String name,
            Coordinates coordinates,
            int enginePower,
            VehicleType type,
            FuelType fuelType
    ) {
        checkInputData(name, coordinates, enginePower, fuelType);

        this.id = nextId++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.enginePower = enginePower;
        this.type = type;
        this.fuelType = fuelType;
    }

    public Vehicle(
            Integer id,
            String name,
            Coordinates coordinates,
            LocalDate creationDate,
            int enginePower,
            VehicleType type,
            FuelType fuelType
    ) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.type = type;
        this.fuelType = fuelType;
    }

    public static void setNextId(Integer nextId) {
        Vehicle.nextId = nextId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public int getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(int enginePower) {
        this.enginePower = enginePower;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * Method to check input data
     * @param name input vehicle's name
     * @param coordinates input vehicle's coordinates
     * @param enginePower input vehicle's engine's power
     * @param fuelType input vehicle's fuel's type
     */
    private void checkInputData(
            String name,
            Coordinates coordinates,
            int enginePower,
            FuelType fuelType
    ) {
        if (name == null) {
            throw new NullPointerException("Argument name is null");
        }

        if (name.isBlank()) {
            throw new IllegalArgumentException(
                    "Argument name can't be empty or contain only white spaces"
            );
        }

        if (coordinates == null) {
            throw new NullPointerException("Argument coordinates is null");
        }

        if (enginePower <= 0) {
            throw new IllegalArgumentException(
                    "Expected enginePower > 0, but enginePower = " + enginePower
            );
        }

        if (fuelType == null) {
            throw new NullPointerException("Argument fuelType is null");
        }
    }

    @Override
    public int compareTo(Vehicle o) {
        int fuelTypeComparing = compareByFuelTypeTo(o.fuelType);
        if (fuelTypeComparing == 0) {
            return Integer.compare(id, o.id);
        }

        return fuelTypeComparing;
    }

    /**
     * Method to compare by fuel's types
     * @param o is object for comparing
     * @return answer
     */
    public int compareByFuelTypeTo(FuelType o) {
        return fuelType.getTypeName().compareTo(o.getTypeName()) * -1;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", enginePower=" + enginePower +
                ", type=" + type +
                ", fuelType=" + fuelType +
                '}';
    }
}
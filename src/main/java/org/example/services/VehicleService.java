package org.example.services;

import org.example.enums.FuelType;
import org.example.models.Vehicle;
import org.example.repositories.VehicleJSONRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for working with vehicles
 */
public class VehicleService {
    private final ArrayDeque<Vehicle> collection;
    private final VehicleJSONRepository repository;
    private final LocalDate initializedDate;

    public VehicleService(
            ArrayDeque<Vehicle> collection,
            VehicleJSONRepository repository
    ) {
        this.collection = collection;
        this.repository = repository;
        this.initializedDate = LocalDate.now();

        int maxInd = 0;
        for (Vehicle vehicle : collection) {
            maxInd = Math.max(maxInd, vehicle.getId() + 1);
        }

        Vehicle.setNextId(maxInd);
    }

    /**
     * Method to add new vehicle to collection
     * @param vehicle is new element
     */
    public void add(Vehicle vehicle) {
        collection.add(vehicle);

    }

    /**
     * Method to remove vehicle from collection by id
     * @param id is id of vehicle to remove
     */
    public void removeById(Integer id) {
        boolean vehicleWasFound = false;
        for (Vehicle vehicle : collection) {
            if (vehicle.getId().equals(id)) {
                collection.remove(vehicle);
                vehicleWasFound = true;
                break;
            }
        }

        if (!vehicleWasFound) {
            throw new IllegalArgumentException(
                    "Collection doesn't exists vehicle with id = " + id.toString()
            );
        }
    }

    /**
     * Method to clean collection
     */
    public void clear() {
        Vehicle.setNextId(0);
        collection.clear();
    }

    /**
     * Method to save current collection to file
     * @throws IOException If an I/O error occurs
     */
    public void save() throws IOException {
        repository.write(this);
    }

    /**
     * Method to remove vehicles, which are greater than input vehicle
     * @param vehicle is inout element
     */
    public void removeGreater(Vehicle vehicle) {
        List<Vehicle> vehiclesToRemove = new ArrayList<>();
        collection.forEach(currVehicle -> {
            if (currVehicle.compareTo(vehicle) > 0) {
                vehiclesToRemove.add(currVehicle);
            }
        });

        vehiclesToRemove.forEach(collection::remove);
        vehiclesToRemove.clear();
    }

    /**
     * Method to remove vehicles, which are lower than input vehicle
     * @param vehicle is inout element
     */
    public void removeLower(Vehicle vehicle) {
        List<Vehicle> vehiclesToRemove = new ArrayList<>();
        collection.forEach(currVehicle -> {
            if (currVehicle.compareTo(vehicle) < 0) {
                vehiclesToRemove.add(currVehicle);
            }
        });

        vehiclesToRemove.forEach(collection::remove);
        vehiclesToRemove.clear();
    }

    /**
     * Method to get average value of engine powers
     * @return average value
     */
    public double averageOfEnginePower() {
        int sumEnginePower = 0;
        for (Vehicle vehicle : collection) {
            sumEnginePower += vehicle.getEnginePower();
        }

        return (double) sumEnginePower / collection.size();
    }

    /**
     * Method to get collection of vehicles, which have fuel's type, which is less than input
     * @param fuelType is input fuel's type
     * @return filtered collection
     */
    public List<Vehicle> filterLessThanFuelType(FuelType fuelType) {
        List<Vehicle> filteredCollection = new ArrayList<>();
        collection.forEach(vehicle -> {
            if (vehicle.compareByFuelTypeTo(fuelType) < 0) {
                filteredCollection.add(vehicle);
            }
        });

        return filteredCollection;
    }

    /**
     * Method to return vehicle from collection by id
     * @param id is vehicle's id
     * @return vehicle
     */
    public Vehicle getById(Integer id) {
        for (Vehicle vehicle : collection) {
            if (vehicle.getId().equals(id)) {
                return vehicle;
            }
        }

        throw new IllegalArgumentException();
    }

    public List<Vehicle> getCollection() {
        return new ArrayList<>(collection);
    }

    public LocalDate getInitializedDate() {
        return initializedDate;
    }
}

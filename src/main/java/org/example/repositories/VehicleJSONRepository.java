package org.example.repositories;

import org.example.enums.FuelType;
import org.example.enums.VehicleType;
import org.example.models.Coordinates;
import org.example.models.Vehicle;
import org.example.services.VehicleService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayDeque;


/**
 * Repository class to save and use information about vehicles in json file
 */
public class VehicleJSONRepository {
    private final String fileName;

    public VehicleJSONRepository(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Method to read saved information in file
     * @return vehicle's service with saved information
     * @throws IOException If an I/O error occurs
     * @throws ParseException if an parse error occurs
     */
    public VehicleService read() throws IOException, ParseException {
        InputStream in = new FileInputStream(fileName);
        BufferedInputStream stream = new BufferedInputStream(in);
        String jsonData = new String(stream.readAllBytes());

        JSONObject object = (JSONObject) new JSONParser().parse(jsonData);

        ArrayDeque<Vehicle> vehicles = new ArrayDeque<>();
        JSONArray array = (JSONArray) object.get("Collection");
        for (Object o : array) {
            JSONObject vehicleJSONObject = (JSONObject) o;

            JSONObject coordinatesJSONObject = (JSONObject) vehicleJSONObject.get("Coordinates");
            Coordinates coordinates = new Coordinates(
                    Integer.parseInt(String.valueOf(coordinatesJSONObject.get("X"))),
                    Integer.parseInt(String.valueOf(coordinatesJSONObject.get("Y")))
            );

            Vehicle vehicle = new Vehicle(
                    Integer.parseInt(String.valueOf(vehicleJSONObject.get("Id"))),
                    (String) vehicleJSONObject.get("Name"),
                    coordinates,
                    LocalDate.parse((String) vehicleJSONObject.get("CreationDate")),
                    Integer.parseInt(String.valueOf(vehicleJSONObject.get("EnginePower"))),
                    VehicleType.valueOf((String) vehicleJSONObject.get("VehicleType")),
                    FuelType.valueOf((String) vehicleJSONObject.get("FuelType"))
            );

            vehicles.add(vehicle);
        }

        VehicleService service = new VehicleService(vehicles, this);

        stream.close();
        in.close();

        return service;
    }

    /**
     * Method to save information about vehicles to file
     * @param service where information is
     * @throws IOException If an I/O error occurs
     */
    public void write(VehicleService service) throws IOException {
        JSONObject object = new JSONObject();

        JSONArray array = new JSONArray();
        service.getCollection().forEach(vehicle -> {
            JSONObject object1 = new JSONObject();
            object1.put("Id", vehicle.getId());
            object1.put("Name", vehicle.getName());

            JSONObject coordinates = new JSONObject();
            coordinates.put("X", vehicle.getCoordinates().getX());
            coordinates.put("Y", vehicle.getCoordinates().getY());
            object1.put("Coordinates", coordinates);

            object1.put("CreationDate", vehicle.getCreationDate().toString());
            object1.put("EnginePower", vehicle.getEnginePower());
            object1.put("VehicleType", vehicle.getType().toString());
            object1.put("FuelType", vehicle.getFuelType().toString());

            array.add(object1);
        });

        object.put("Collection", array);

        FileOutputStream stream = new FileOutputStream(fileName);
        stream.write(object.toJSONString().getBytes());
        stream.close();
    }
}

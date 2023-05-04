package org.example;

import org.example.console.Console;
import org.example.repositories.VehicleJSONRepository;
import org.example.services.VehicleService;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main application class
 */
public class Main {
    public static void main(String[] args) {
        try {
            VehicleJSONRepository repository = new VehicleJSONRepository("file.txt");
            VehicleService service = repository.read();
            Console console = new Console(service, new InputStreamReader(System.in));
            console.run();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
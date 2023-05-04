package org.example.console;

import org.example.enums.FuelType;
import org.example.enums.VehicleChangeParam;
import org.example.enums.VehicleType;
import org.example.models.Coordinates;
import org.example.models.Vehicle;
import org.example.services.VehicleService;

import java.io.*;
import java.util.*;

/**
 * Console class
 */
public class Console {
    private final List<String> history;
    private final VehicleService service;
    private final BufferedReader bufferedReader;
    private final List<String> fileStrings = new ArrayList<>();

    public Console(VehicleService service, Reader reader) {
        history = new ArrayList<>();
        this.service = service;
        this.bufferedReader = new BufferedReader(reader);
    }

    /**
     * Method to run console application
     *
     * @throws IOException If an I/O error occurs
     */
    public void run() throws IOException {
        while (true) {
            try {
                String command = getCommand();
                if (command.equals("help")) {
                    help();
                } else if (command.equals("info")) {
                    info();
                } else if (command.equals("show")) {
                    show();
                } else if (command.equals("add")) {
                    service.add(createVehicle());
                    System.out.println("Vehicle is done!");
                } else if (command.startsWith("update")) {
                    Integer id = Integer.parseInt(command.split(" ")[1]);
                    Vehicle vehicle = service.getById(id);
                    update(vehicle);
                    System.out.println("Vehicle is updated!");
                } else if (command.startsWith("remove_by_id")) {
                    Integer id = Integer.parseInt(command.split(" ")[1]);
                    service.removeById(id);
                    System.out.println("Vehicle is removed!");
                } else if (command.equals("clear")) {
                    service.clear();
                } else if (command.equals("save")) {
                    service.save();
                } else if (command.startsWith("execute_script")) {
                    String fileName = command.split(" ")[1];
                    executeScript(fileName);
                } else if (command.equals("exit")) {
                    bufferedReader.close();
                    break;
                } else if (command.equals("remove_greater")) {
                    service.removeGreater(createVehicle());
                    System.out.println("Элементы удалены!");
                } else if (command.equals("remove_lower")) {
                    service.removeLower(createVehicle());
                    System.out.println("Элементы удалены!");
                } else if (command.equals("history")) {
                    history();
                } else if (command.equals("average_of_engine_power")) {
                    System.out.println(service.averageOfEnginePower());
                } else if (command.startsWith("filter_less_than_fuel_type")) {
                    FuelType fuelType = FuelType.valueOf(command.split(" ")[1]);
                    List<Vehicle> vehicles = service.filterLessThanFuelType(fuelType);
                    if (vehicles.size() == 0) {
                        System.out.println("Элементов не найдено");
                    } else {
                        vehicles.forEach(System.out::println);
                    }
                } else if (command.equals("print_descending")) {
                    List<Vehicle> vehicles = service.getCollection();
                    if (vehicles.size() == 0) {
                        System.out.println("Элементов не найдено");
                    } else {
                        vehicles.sort(Vehicle::compareTo);
                        Collections.reverse(vehicles);
                        vehicles.forEach(System.out::println);
                    }
                } else {
                    throw new IllegalArgumentException();
                }

                history.add(command);
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Неверная команда, попробуйте ещё");
            } catch (IOException e) {
                System.out.println("Неверное имя файла, попробуйте ещё");
            }
        }
    }

    /**
     * Method to call function help
     */
    private void help() {
        StringBuilder help = new StringBuilder()
                .append("help : вывести справку по доступным командам\n")
                .append("info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n")
                .append("show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n")
                .append("add {element} : добавить новый элемент в коллекцию\n")
                .append("update id {element} : обновить значение элемента коллекции, id которого равен заданному\n")
                .append("remove_by_id id : удалить элемент из коллекции по его id\n")
                .append("clear : очистить коллекцию\n")
                .append("save : сохранить коллекцию в файл\n")
                .append("execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n")
                .append("exit : завершить программу (без сохранения в файл)\n")
                .append("remove_greater {element} : удалить из коллекции все элементы, превышающие заданный\n")
                .append("remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n")
                .append("history : вывести последние 5 команд (без их аргументов)\n")
                .append("average_of_engine_power : вывести среднее значение поля enginePower для всех элементов коллекции\n")
                .append("filter_less_than_fuel_type fuelType : вывести элементы, значение поля fuelType которых меньше заданного\n")
                .append("print_descending : вывести элементы коллекции в порядке убывания");
        System.out.println(help);
    }

    /**
     * Method to call function info
     */
    private void info() {
        StringBuilder info = new StringBuilder()
                .append("Тип : ").append(ArrayDeque.class).append('\n')
                .append("Дата инициализации : ").append(service.getInitializedDate()).append('\n')
                .append("Количество элементов : ").append(service.getCollection().size()).append('\n');
        System.out.print(info);
    }

    /**
     * Method to call function show
     */
    private void show() {
        List<Vehicle> vehicles = service.getCollection();
        if (vehicles.size() == 0) {
            System.out.println("Элементов не найдено");
        } else {
            vehicles.forEach(System.out::println);
        }
    }

    /**
     * Method to read name
     *
     * @return name
     * @throws IOException If an I/O error occurs
     */
    private String getName() throws IOException {
        while (true) {
            System.out.println("Введите имя :");
            try {
                String name = getCommand();
                if (name.isBlank()) {
                    throw new IllegalArgumentException();
                }

                return name;
            } catch (IllegalArgumentException e) {
                System.out.println("Имя не должно быть пустым, попробуйте ещё раз");
            }
        }
    }

    /**
     * Method to read coordinates
     *
     * @return coordinates
     * @throws IOException If an I/O error occurs
     */
    private Coordinates getCoordinates() throws IOException {
        Integer x;
        Integer y;

        while (true) {
            System.out.println("Введите координату X (больше -576):");
            try {
                x = Integer.parseInt(getCommand());
                if (x.compareTo(-576) <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат координаты X, попробуйте ещё");
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println("Аргумент X должен быть больше -576, попробуйте ещё");
                continue;
            }

            break;
        }

        while (true) {
            System.out.println("Введите координату Y (больше -286):");
            try {
                y = Integer.parseInt(getCommand());
                if (y.compareTo(-286) <= 0) {
                    throw new IllegalArgumentException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный формат координаты Y, попробуйте ещё");
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println("Аргумент Y должен быть больше -286, попробуйте ещё");
                continue;
            }

            break;
        }

        return new Coordinates(x, y);
    }

    /**
     * Method to read engine's power
     *
     * @return engine's power
     * @throws IOException If an I/O error occurs
     */
    private int getEnginePower() throws IOException {
        while (true) {
            System.out.println("Введите мощность двигателя (больше 0) :");
            try {
                int enginePower = Integer.parseInt(getCommand());
                if (enginePower <= 0) {
                    System.out.println("Мощность двигателя должна быть больше 0");
                    continue;
                }

                return enginePower;
            } catch (NumberFormatException e) {
                System.out.println("Мощность двигателя должна быть числом, попробуйте ещё");
            }
        }
    }

    /**
     * Method to read vehicle's type
     *
     * @return vehicle's type
     * @throws IOException If an I/O error occurs
     */
    private VehicleType getVehicleType() throws IOException {
        while (true) {
            StringBuilder vehicleTypeQuestion = new StringBuilder()
                    .append("Введите тип транспортного средства (заполнять не обязательно) :\n")
                    .append("Возможные варианты : \n")
                    .append("PLANE\n")
                    .append("BOAT\n")
                    .append("BICYCLE\n")
                    .append("SPACESHIP");
            System.out.println(vehicleTypeQuestion);
            try {
                String vehicleTypeString = getCommand();
                VehicleType vehicleType;
                if (vehicleTypeString.isEmpty()) {
                    vehicleType = null;
                } else {
                    vehicleType = VehicleType.valueOf(vehicleTypeString);
                }

                return vehicleType;
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный тип транспортного средства, попробуйте ещё");
            }
        }
    }

    /**
     * Method to read fuel's type
     *
     * @return fuel's type
     * @throws IOException If an I/O error occurs
     */
    private FuelType getFuelType() throws IOException {
        while (true) {
            StringBuilder fuelTypeQuestion = new StringBuilder()
                    .append("Введите тип топлива :\n")
                    .append("Возможные варианты : \n")
                    .append("GASOLINE\n")
                    .append("KEROSENE\n")
                    .append("MANPOWER\n")
                    .append("PLASMA");
            System.out.println(fuelTypeQuestion);
            try {
                return FuelType.valueOf(getCommand());
            } catch (IllegalArgumentException e) {
                System.out.println("Неверный вид топлива, попробуйте ещё");
            }
        }
    }

    /**
     * Method to create new Vehicle from console
     *
     * @return vehicle
     * @throws IOException If an I/O error occurs
     */
    private Vehicle createVehicle() throws IOException {
        return new Vehicle(
                getName(),
                getCoordinates(),
                getEnginePower(),
                getVehicleType(),
                getFuelType()
        );
    }

    /**
     * Change 1 parameter in vehicle
     *
     * @param vehicle for updating
     * @param param   to change
     * @throws IOException If an I/O error occurs
     */
    private void change(Vehicle vehicle, VehicleChangeParam param)
            throws IOException {
        while (true) {
            System.out.println("Хотите поменять " + param.getParam() + " (да / нет) ?");
            String ans = getCommand();
            if (ans.equals("да")) {
                switch (param) {
                    case NAME -> vehicle.setName(getName());
                    case COORDINATES -> vehicle.setCoordinates(getCoordinates());
                    case ENGINE_POWER -> vehicle.setEnginePower(getEnginePower());
                    case VEHICLE_TYPE -> vehicle.setType(getVehicleType());
                    case FUEL_TYPE -> vehicle.setFuelType(getFuelType());
                }

                break;
            } else if (ans.equals("нет")) {
                break;
            }

            System.out.println("Некорректный ответ");
        }
    }

    /**
     * Method to call function update
     *
     * @param vehicle for updating
     * @throws IOException If an I/O error occurs
     */
    private void update(Vehicle vehicle)
            throws IOException {
        change(vehicle, VehicleChangeParam.NAME);
        change(vehicle, VehicleChangeParam.COORDINATES);
        change(vehicle, VehicleChangeParam.ENGINE_POWER);
        change(vehicle, VehicleChangeParam.VEHICLE_TYPE);
        change(vehicle, VehicleChangeParam.FUEL_TYPE);
    }

    /**
     * Method to call function execute_script
     *
     * @param fileName is name of file with script
     * @throws IOException If an I/O error occurs
     */
    private void executeScript(String fileName) throws IOException {
        int ind = 0;
        BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = fileReader.readLine()) != null) {
            fileStrings.add(ind++, line);
        }

        fileReader.close();
    }

    /**
     * Method to call function history
     */
    private void history() {
        StringBuilder ans = new StringBuilder();
        for (int i = Math.max(0, history.size() - 5); i < history.size(); i++) {
            ans.append(history.get(i)).append('\n');
        }

        System.out.print(ans);
    }

    /**
     * Method to get command from console or from file
     *
     * @return command
     * @throws IOException If an I/O error occurs
     */
    private String getCommand() throws IOException {
        if (fileStrings.size() > 0) {
            return fileStrings.remove(0);
        }

        return bufferedReader.readLine();
    }
}

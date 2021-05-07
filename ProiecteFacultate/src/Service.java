import java.io.IOException;
import java.util.*;

public class Service {
    public static final String CAR_FILE = "car.csv";
    public static final String CREATE_CAR_FILE = "createcar.csv";
    public static final String DEALERSHIP_FILE = "dealerships.csv";
    public static final String CREATE_DEALERSHIP_FILE = "createdealership.csv";
    public static final String AUDIT_FILE = "audit.csv";

    private final CreateDealerShip createDealerShip = new CreateDealerShip();
    private final CreateCar createCar = new CreateCar();
    private final Set<DealerShip> dealerships = new TreeSet<>();

    // 1. La primul pas dorim sa cream un dealership nou
    public void newDealerShip() throws IOException{
        CreateDealerShip x = new CreateDealerShip();
        DealerShip y = CreateDealerShip.newDealerShip();
        dealerships.add(y);
        log("createdealership");
    }

    // 2. Vrem sa adaugam masini in reprezentante
    public void addCarInDS(int dealershipId) throws Eroare, IOException {
        DealerShip x = getDSById(dealershipId);
        CreateCar car = new CreateCar();
        Car y = CreateCar.newCar();

        x.addCar(y);
        log("addcar");
    }

    // 3. Vrem sa afisam toate masinile dintr-o anumita reprezentanta
    public void showCars(int dealershipId) throws Eroare, IOException {
        System.out.println(getDSById(dealershipId).getCarDetails());
        log("showcars");
    }

    // 4. Vrem sa afisam masinile dintr-o reprezentanta in ordine descrescatoare a anilor
    public void sortCars(int dealershipId) throws Eroare {
        getDSById(dealershipId).sortCarsYear(); // Mai intai o sortez
        getDSById(dealershipId).reverseList(); // Apoi o inversez
    }

    // 5. Ma intereseaza motorizarea cea mai frecvent dorita
    public String bestFuelType() {
        Map<String, Integer> popular = new HashMap<>();
        Integer Max = -1;
        String motorizare = "No se puede mi amigo";

        // intru in dealership-uri pe rand
        for(DealerShip x: dealerships) {
            // pentru fiecare masina din dealership verif cea mai frecventa motorizare
            for(Car y: x.getCars()) {
                String fuelType = y.getFuelType();
                if(popular.containsKey(fuelType)) {
                    popular.put(fuelType, popular.get(fuelType) + 1);
                }
                else {
                    popular.put(fuelType, 1);
                }

                if(popular.get(fuelType) > Max) {
                    Max = popular.get(fuelType);
                    motorizare = fuelType;
                }
            }
        }
        return motorizare;
    }

    // 6. Vreau sa afisez toate masinile in functie de motorizare, intrucat am vazut inainte care este cea mai populara motorizare
    public int showLikes(String fuelType) throws Eroare {
        Map<String, Integer> exists = new HashMap<>();

        for(DealerShip x: dealerships) {
            for(Car y: x.getCars()) {
                fuelType = y.getFuelType();
                if(exists.containsKey(fuelType)) {
                    exists.put(fuelType, exists.get(fuelType) + 1);
                }
                else {
                    exists.put(fuelType, 1);
                }
                if(exists.get(fuelType) >= 1) {
                    return 1;
                }
            }
        }
        return 0;
    }

    // 7. Vreau sa pot verifica daca o masina exista sau nu intr-un dealership, in functie de nume
    public boolean carExists(int dealershipId, String carName) throws Eroare {
        return getDSById(dealershipId).carExists(carName);
    }

    // 8. Vreau sa pot vinde o masina creata anterior
    public void sellCar(int dealershipId, String carName) throws Eroare {
        DealerShip x = getDSById(dealershipId);
        x.sellCar(carName);
    }

    // 9. Vreau sa am optiunea sa elimin toate masinile adaugate anterior intr-un dealership
    public void clearOutDS(int dealershipId) throws Eroare {
        getDSById(dealershipId).clearDS();
    }

    private DealerShip getDSById(int dealershipId) throws Eroare {
        for(DealerShip x: dealerships) {
            if(x.getId() == dealershipId) {
                return x;
            }
        }
        throw new Eroare("This dealership does not exist.");
    }

    public void load() throws IOException {
        createDealerShip.readDataFromFile(CREATE_DEALERSHIP_FILE, 0);
        createCar.readDataFromFile(CREATE_CAR_FILE, 0);

        //CreateCar x = new CreateCar();
        //x.readDataFromFile(CREATE_CAR_FILE, 0);

        int position = 0;
        Read nanu = Read.getInstance();
        String[] interior1 = nanu.readLine(DEALERSHIP_FILE, position);
        if(interior1 != null) {
            for (String s: interior1) {
                position += s.length() + 1;
            }
            position += 1; // pentru caracterul new line
            
            while (position != -1) {
                DealerShip s = new DealerShip();
                position = s.readDataFromFile(DEALERSHIP_FILE, position);
                if (position != -1) {
                    dealerships.add(s);
                }
            }
        }

        ArrayList<Car> cars = createCar.readCarsFromFile(CAR_FILE);
        for (Car c: cars) {
            try {
                DealerShip ds = getDSById(c.getDealershipID());
                ds.addCar(c);
            } catch (Eroare eroare) {
                throw new IOException("Invalid file");
            }
        }
    }

    public void save() throws IOException {
        Write nanu = Write.getInstance();

        // Clear the files content.
        nanu.delete(CAR_FILE);
        nanu.delete(DEALERSHIP_FILE);
        nanu.delete(CREATE_CAR_FILE);
        nanu.delete(CREATE_DEALERSHIP_FILE);

        // Save the new state.
        createDealerShip.writeDataInFile(CREATE_DEALERSHIP_FILE);
        createCar.writeDataInFile(CREATE_CAR_FILE);
        for (DealerShip ds: dealerships) {
            ds.writeDataInFile(DEALERSHIP_FILE);
            for (Car c: ds.getCars()) {
                c.writeDataInFile(CAR_FILE);
            }
        }
    }

    private void log(String action) throws IOException {
        Write nanu = Write.getInstance();
        String timestamp = String.valueOf(System.currentTimeMillis());
        nanu.write(AUDIT_FILE, action + ", " + timestamp + ",\n");
    }
}
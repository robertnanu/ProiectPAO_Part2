import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

public class CreateCar extends Headquarters {
    private static int NEW_ID = 0;
    // Am adaugat toate modelele posibile intr-un map
    private static HashMap<String, Supplier<Car>> existingCars = new HashMap<>() {{
        put("audi", Audi::new);
        put("bmw", Bmw::new);
        put("jaguar", Jaguar::new);
        put("jeep", Jeep::new);
        put("mercedes", Mercedes::new);
        put("peugeot", Peugeot::new);
        put("volkswagen", Volkswagen::new);
        put("volvo", Volvo::new);
    }};

    public static Car newCar() {

        Car x;

        Scanner f = new Scanner(System.in);

        System.out.println("Car model: ");
        while(true) {
            String model = f.nextLine();
            if(existingCars.containsKey(model)) {
                x = existingCars.get(model).get();
                break;
            }

            System.out.print("The only existing car models are: ");
            existingCars.forEach((type, value) -> System.out.print(type + "; "));
            System.out.println("\n");
        }
        addToHistory(NEW_ID);
        x.setId(NEW_ID++);

        //NEW_ID++;

        System.out.println("Car with id: " + x.getId() + " was successfully created!");

        return x;
    }

    public ArrayList<Car> readCarsFromFile(String file) throws IOException {
        ArrayList<Car> c = new ArrayList<Car>();
        Read nanu = Read.getInstance();
        int position = 0;
        String[] interior1 = nanu.readLine(file, position);
        if(interior1 != null) {
            for(String x: interior1) {
                position += x.length() + 1;
            }
            position += 1;
            while(true) {
                String[] carInfo = nanu.readLine(file, position);
                if(carInfo == null) {
                    break;
                }
                Car rob = existingCars.get(carInfo[2].toLowerCase()).get();
                position = rob.readDataFromFile(file, position);
                c.add(rob);
            }
        }

        return c;
    }

    @Override
    int getNewID() {
        return NEW_ID;
    }

    @Override
    void setNewID(int id) {
        NEW_ID =id;
    }
}

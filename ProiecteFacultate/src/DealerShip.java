import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DealerShip implements Comparable<DealerShip>, CSV {
    private int dealershipId;
    private String name;
    private final ArrayList<Car> cars = new ArrayList<>();
    private int year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return dealershipId;
    }

    public void setId(int dealershipId) {
        this.dealershipId = dealershipId;
    }

    // afisarea tuturor detaliilor unei masini
    public String getCarDetails() {
        return cars.toString();
    }

    // Imi returneaza masinile din colectie
    public ArrayList<Car> getCars() {
        return cars;
    }

    // Imi returneaza daca exista masina pe care vreau sa o caut dupa nume
    public boolean carExists(String name) {
        return itExists(name) != null;
    }

    // Primeste un nume si verifica daca masina exista
    private Car itExists(String name) {
        for(Car x: cars) {
            if(x.getModel().equals(name)) {
                return x;
            }
        }
        return null;
    }

    // Functie care imi ordoneaza masinile dupa anumite criterii
    public void sortCarsYear() {
        cars.sort(Comparator.comparing(Car::getFabricationYear));
    }

    // Am nevoie de o ordonare descrescatoare in functie de anul fabricatiei
    public void reverseList() {
        Collections.reverse(cars);
    }

    // Functie care imi adauga o masina noua in colectie
    public void addCar(Car x) {
        this.cars.add(x);
    }

    // Trebuie sa pot sterge toate masinile adaugate intr-un dealership
    public void clearDS() {
        cars.clear();
    }

    // Functie care verifica daca exista o masina cu un anumit nume si o elimina din colectie daca exista
    public void sellCar(String name) throws Eroare {
        Car x = itExists(name);
        if(x == null) {
            throw new Eroare("This car is not available in the dealership you have selected");
        }
        year += x.getFabricationYear();
        cars.remove(x);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int compareTo(DealerShip o) {
        if(o.year == year) {
            return dealershipId - o.dealershipId;
        }
        return o.year - year;
    }

    @Override
    public int readDataFromFile(String file, int position) throws IOException {
        Read nanu = Read.getInstance();
        String[] interior = nanu.readLine(file, position);
        if (interior == null) {
            return -1;
        }
        dealershipId = Integer.parseInt(interior[0]);
        name = interior[1];

        for (String s: interior) {
            position += s.length() + 1;
        }

        return position + 1;
    }

    @Override
    public void writeDataInFile(String file) throws IOException {
        Write nanu = Write.getInstance();
        String headers = "id,name,\n";
        String body = dealershipId + "," + name + ",\n";
        if (nanu.isEmpty(file)) {
            nanu.write(file, headers + body);
        }
        else {
            nanu.write(file, body);
        }
    }
}

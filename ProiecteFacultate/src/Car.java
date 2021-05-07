import java.io.IOException;

public abstract class Car implements CSV {
    private int carId, dealershipID;

    abstract String getModel();
    abstract int getPrice();
    abstract String getFuelType();
    abstract int getFabricationYear();

    public int getId() {
        return carId;
    }

    public void setId(int carId) {
        this.carId = carId;
    }

    @Override
    public String toString() {
        return "Car id: " + carId + ", model: " + getModel() + " fabricated in: " + getFabricationYear() + ", is listed at: " + getPrice() + "$ and runs on: " + getFuelType();
    }

    public int getDealershipID() {
        return dealershipID;
    }

    public void setDealershipID(int dealershipID) {
        this.dealershipID = dealershipID;
    }

    @Override
    public int readDataFromFile(String file, int position) throws IOException {
        Read nanu = Read.getInstance();

        String[] interior = nanu.readLine(file, position);
        if(interior == null)
            return -1;
        
        carId = Integer.parseInt(interior[0]);
        dealershipID = Integer.parseInt(interior[1]);

        for(String x: interior)
            position += x.length() + 1;

        position += 1;

        return position;
    }

    @Override
    public void writeDataInFile(String file) throws IOException {
        Write nanu = Write.getInstance();
        String interior1 = "carID,DealershipID,model,\n";
        String interior2 = carId + "," + dealershipID + "," + getModel() + ",\n";

        if(nanu.isEmpty(file))
        {
            nanu.write(file, interior1 + interior2);
        }
        else {
            nanu.write(file, interior2);
        }
    }    
}

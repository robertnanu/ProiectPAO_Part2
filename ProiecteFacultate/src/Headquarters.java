import java.io.IOException;
import java.util.ArrayList;

public abstract class Headquarters implements CSV {
    private final static ArrayList<Integer> history = new ArrayList<>();
    abstract int getNewID();
    abstract void setNewID(int id);

    
    @Override
    public int readDataFromFile(String file, int position) throws IOException {
        Read nanu = Read.getInstance();

        String[] interior1 = nanu.readLine(file, position);
        for(String x: interior1) {
            position += x.length() + 1;
        }
        position += 1;

        String[] interior2 = nanu.readLine(file, position);

        setNewID(Integer.parseInt(interior2[0]));

        for(String x: interior2)
            position += x.length() + 1;

        position += 1;

        return position;
    }

    @Override
    public void writeDataInFile(String file) throws IOException {
        // urmeaza
        Write nanu = Write.getInstance();
        nanu.write(file, "newID,\n" + getNewID() + ",\n");
    }

    protected static void addToHistory(Integer id) {
        history.add(id);
    }

    protected ArrayList<Integer> getHistory() {
        return history;
    }
}

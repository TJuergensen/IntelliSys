import java.util.ArrayList;
import java.util.List;

public class DataContainer {
    List<Integer> data;
    DataContainer() {
        clear();
    }

    synchronized void addInt(int add) {
        data.add(add);
    }

    List<Integer> getDataContainer() {
        return this.data;
    }

    void clear() {
        data = new ArrayList<>();
    }
}

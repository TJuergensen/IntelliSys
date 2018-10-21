import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataContainer {
    List<Integer> data;
    int pointer = 0;
    DataContainer() {
        data = new ArrayList<>();
    }

    synchronized void addInt(int add) {
        data.add(add);
    }

    List<Integer> getDataContainer() {
        return this.data;
    }
}

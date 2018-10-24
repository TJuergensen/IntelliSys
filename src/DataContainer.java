
class DataContainer {
    private int data[];
    private int pointer;
    private int dataSize;
    DataContainer(int dataSize) {
        this.dataSize = dataSize;
        clear();
    }

    synchronized void addInt(int add) {
        data[pointer++] = add;
    }

    int[] getDataContainer() {
        return this.data;
    }

    void clear() {
        data = new int[dataSize];
        pointer = 0;
    }
}

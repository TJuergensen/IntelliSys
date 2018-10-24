import java.util.List;

class DataContainer {
    private int data[]; //Jedes Experiment die Laufzeit in Tagen
    private int name[]; //TODO umbennenen und beschreiben
    private int pointer;
    private int sampleSize;
    private  int maxDaysToRun;
    DataContainer(int sampleSize, int maxDaysToRun) {
        this.sampleSize = sampleSize;
        this.maxDaysToRun = maxDaysToRun;
        clear();
    }

    synchronized void addInt(int day) {
        data[pointer++] = day;
    }

    int[] getDataContainer() {
        return this.data;
    }

    synchronized void addViewCountOnDay(int day, int viewCount) {
        name[day] += viewCount;
    }

    void clear() {
        data = new int[sampleSize];
        name = new int[maxDaysToRun];

        pointer = 0;
    }
}

import java.util.Collections;

/**
 * Holds all of the data, that is created in the simulation.
 * After the simulation is finished, this data is used to create the charts.
 */
class DataContainer {
    private int data[]; //Jedes Experiment die Laufzeit in Tagen
    private int personsWithViewChangeTilThisDay[];
    private int pointer;    //Used to keep track of the position in the data
    private int sampleSize;
    private int maxDaysToRun;

    //Added viewACountToday for testing issues
    private int viewACountToday[];


    /**
     * Creates an empty DataContainer.
     * @param sampleSize Param that's specifies the amount of data in the simulations.
     * @param maxDaysToRun Param that's specifies the max days, a simulation is running.
     */
    DataContainer(int sampleSize, int maxDaysToRun) {
        this.sampleSize = sampleSize;
        this.maxDaysToRun = maxDaysToRun;
        clear();
    }

    /**
     * Adds the day, a simulation is finished to the DataContainer.
     * @param day Param with the day the simulation is finished.
     */
    synchronized void addInt(int day) {
        data[pointer++] = day;
    }

    /**
     * Returns the DataContainer, that holds the information, on which days each simulation finished.
     * @return Returns the DataContainer
     */
    int[] getDataContainer() {
        return this.data;
    }

    /**
     * Adds the amount of people, that have changed their view til that given day, to the total amount of people
     * that have changed their view til that given day across all simulations.
     * @param day Param with the day, the viewCount is associated with.
     * @param viewCount Param with the people, that have changed their view til that given day
     */
    synchronized void addViewCountOnDay(int day, int viewCount) {

        viewACountToday[day] = viewCount;
        personsWithViewChangeTilThisDay[day] += viewCount;
    }

    /**
     * Clears the DataContainer, so that it can be used again.
     */
    void clear() {
        data = new int[sampleSize];
        personsWithViewChangeTilThisDay = new int[maxDaysToRun];
        viewACountToday = new int [maxDaysToRun];
        pointer = 0;
    }

    public int[] getViewACountToday()
    {
        return viewACountToday;
    }
}



/**
 * Holds all of the data, that is created in the simulation.
 * After the simulation is finished, this data is used to create the charts.
 */
class DataContainer {
    private int personsWithViewChangeTilThisDay[];
    private  int maxDaysToRun;

    /**
     * Creates an empty DataContainer.
     * @param maxDaysToRun Param that's specifies the max days, a simulation is running.
     */
    DataContainer(int maxDaysToRun) {
        this.maxDaysToRun = maxDaysToRun;
        clear();
    }

    /**
     * Adds the amount of people, that have changed their view til that given day, to the total amount of people
     * that have changed their view til that given day across all simulations.
     * @param day Param with the day, the viewCount is associated with.
     * @param viewCount Param with the people, that have changed their view til that given day
     */
    synchronized void addViewCountOnDay(int day, int viewCount) {
        personsWithViewChangeTilThisDay[day] += viewCount;
    }

    int[] getPersonsWithViewChangeTilThisDay() {
        return this.personsWithViewChangeTilThisDay;
    }

    /**
     * Clears the DataContainer, so that it can be used again.
     */
    void clear() {
        personsWithViewChangeTilThisDay = new int[maxDaysToRun];
    }
}

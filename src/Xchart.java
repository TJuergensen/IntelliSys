import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

/**
 * Plotter class, that generates Charts with help from the chart library chart.
 * Read more at https://knowm.org/open-source/xchart/
 */
class Xchart {
    private int array[];

    /**
     * Constructor for Xchart
     */
    Xchart() {

    }

    /***
     * Used to compute given Data and create a Chart.
     * @param dataContainer Data structure containing relevant information for plotting
     * @param name Name of the chart, which is used in creating a file for that chart
     * @param sampleSize Number of samples used
     * @throws IOException Throws IOException, while saving a chart as SVG
     */
    void simpleChart(DataContainer dataContainer, String name, long sampleSize)throws IOException {
        //Group all data, with the day there finished as key
        List<Integer> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();

        int[] viewChangesPerDay = dataContainer.getPersonsWithViewChangeTilThisDay();
        boolean firstViewChange = false;
        boolean lastViewChange = false;
        for(int day = 0; day < viewChangesPerDay.length; day++) {
            if(!firstViewChange) {
                if(viewChangesPerDay[day] > 0) {
                    firstViewChange = true;
                }
                xData.add(day);
                yData.add((int) (viewChangesPerDay[day] / (double) sampleSize));
            }else {
                if(viewChangesPerDay[day] > 0) {
                    if(!lastViewChange) {
                        if((int) (viewChangesPerDay[day] / (double) sampleSize) >= 50) {
                            lastViewChange = true;
                            xData.add(day);
                            yData.add((int) (viewChangesPerDay[day] / (double) sampleSize));
                        }else {
                            xData.add(day);
                            yData.add((int) (viewChangesPerDay[day] / (double) sampleSize));
                        }
                    }
                }
            }
        }
        // Create Chart
        XYChart chart = QuickChart.getChart(name, "Past Days", "Persons with View A", " ", xData, yData);
        saveChart(chart, name ,sampleSize);
    }

    /**
     * Saves a given chart, as SVG and autogenerats the name for that file.
     * @param chart Chart used
     * @param name name for the chart
     * @param sampleSize number of samples
     * @throws IOException Throws IOException, while saving a chart as SVG
     */
    private void saveChart(Chart chart, String name, long sampleSize)throws IOException {
        name += "_SampleSize" + sampleSize + "_Date" +new Timestamp(System.currentTimeMillis()).toString();
        name = name.replaceAll(":","-");
        name = name.replaceAll("\\.","-");

        String os = System.getProperty("os.name").toLowerCase();
        String path = "";
        //TODO für windows funktioniert es, aber der ordner output muss noch von hand angelegt werden am anfang. Den rest kann ich nicht überprüfen
        if(os.contains("windows")){
            path = "src\\output\\";
        }else if(os.contains("nux")) {
            path = "./src/output/";
        }else if(os.contains("mac")) {
            path = "./src/output/";
        }
        VectorGraphicsEncoder.saveVectorGraphic(chart, path + name, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
    }

    /**
     * Uses QuickSort for sorting an array
     * @author http://www.java2novice.com/java-sorting-algorithms/quick-sort/ ,24.10.2018
     * @param inputArr the input to be sorted
     */

    private void sort(int[] inputArr) {
        int length;
        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        quickSort(0, length - 1);
    }


    /**
     * QuickSort implementation.
     * @author http://www.java2novice.com/java-sorting-algorithms/quick-sort/ ,24.10.2018
     * @param lowerIndex lower index param
     * @param higherIndex high index param
     */
    private void quickSort(int lowerIndex, int higherIndex) {

        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = array[lowerIndex+(higherIndex-lowerIndex)/2];
        // Divide into two arrays
        while (i <= j) {
            /*
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (array[i] < pivot) {
                i++;
            }
            while (array[j] > pivot) {
                j--;
            }
            if (i <= j) {
                exchangeNumbers(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }

    /**
     * Used by quickSort implementation. change i and j
     * @author http://www.java2novice.com/java-sorting-algorithms/quick-sort/ ,24.10.2018
     * @param i index i to be swapped
     * @param j index j to be swapped
     */
    private void exchangeNumbers(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

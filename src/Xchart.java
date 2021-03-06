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
     * Constructor for Xchart. Is empty, stays empty.
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

        int data[] = dataContainer.getViewACountToday(); //an array with maximum days to run fields, each containing the ammount of people with viewA
        //double[] xData = new double[data.length]; //xData represents each day
        //double[] yDataCumulated = new double[data.length]; //yDataCumulated represents cumulated ammount of people with view A
        List<Double> xData = new ArrayList<>();
        List<Double> yDataCumulated = new ArrayList<>();

        //pre-initialize x- and y data with first values
        xData.add(0.0);
        yDataCumulated.add((double)data[0]);

        for(int i = 1; i< data.length; i++)
        {
            //Initialize a list of days for chart usage


            //cumulate people with viewA
            if(data[i] != 0)
            {
                xData.add((double) i);
                yDataCumulated.add((double)data[i-1] + data[i] / (double) sampleSize);
            }
            //yDataCumulated[i] = (data[i-1] + data[i]);
        }

        XYChart chart = QuickChart.getChart("Samples finished", "Days", "People with view A", " ", xData, yDataCumulated);
        saveChart(chart, name + "_PerDay",sampleSize);

        /*
        //Group all data, with the day there finished as key
        array = dataContainer.getDataContainer();
        sort(array);
        List<Integer> xData = new ArrayList<>();
        List<Integer> yDataPerDay = new ArrayList<>();
        List<Integer> yDataSinceDayOne = new ArrayList<>();
        int lastInt = -1;
        int countIntPerDay = 1;
        int countIntTotal = 1;

        //Iterate over the given data and allocate it for the chart creation
        for(Integer i : array) {
            if(i != lastInt) {
                if(lastInt != -1) {
                    xData.add(lastInt);
                    yDataPerDay.add(countIntPerDay);
                    yDataSinceDayOne.add(countIntTotal);
                }
                lastInt = i;
                countIntPerDay = 1;
            }else {
                countIntPerDay++;
            }
            countIntTotal++;
        }
        // Create Chart
        XYChart chart = QuickChart.getChart("Samples finished", "Past Days", "Samples finished per Day", " ", xData, yDataPerDay);
        saveChart(chart, name + "_PerDay",sampleSize);
        chart = QuickChart.getChart("Total Samples finished", "Past Days", "Total Samples finished over Time", " ", xData, yDataSinceDayOne);
        saveChart(chart, name + "_OverTime",sampleSize);
        */
    }

    /**
     * Saves a given chart in output folder as SVG and autogenerats the name for that file. OS-independend (MacOS not tested)
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
        //TODO Works for mac?
        if(os.contains("windows")){
            path = "src\\output\\";
        }else if(os.contains("nux")) {
            path = "./src/output/";
        }else if(os.contains("mac")) {
            path = "./src/output/";
        }
        VectorGraphicsEncoder.saveVectorGraphic(chart, path + name, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
    }

    //---ToDo ---- Rest needed?------------------------------------------------------------------------------------------

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

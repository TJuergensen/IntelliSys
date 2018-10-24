import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

class Xchart {
    private int array[];
    private int length;
    Xchart() {

    }

    /***
     * Used to compute given Data and create a Chart
     * @param dataContainer Data structure containing relevant information for plotting
     * @param name Name of the chart output ToDO VERIFIZIEREN! Stimmt diese info?
     * @param sampleSize Number of samples used
     * @throws IOException
     */
    void simpleChart(DataContainer dataContainer, String name, long sampleSize)throws IOException {
        //Group all data, with the day there finished as key
        array = dataContainer.getDataContainer();
        sort(array);
        List<Integer> xData = new ArrayList<>();
        List<Integer> yDataPerDay = new ArrayList<>();
        List<Integer> yDataSinceDayOne = new ArrayList<>();
        int lastInt = -1;
        int countIntPerDay = 1;
        int countIntTotal = 1;
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
    }

    /***
     *
     * @param chart Chart used
     * @param name name for the chart
     * @param sampleSize number of samples
     * @throws IOException
     */
    void saveChart(Chart chart, String name, long sampleSize)throws IOException {
        name += "_SampleSize" + sampleSize + "_Date" +new Timestamp(System.currentTimeMillis()).toString();
        name = name.replaceAll(":","-");
        name = name.replaceAll("\\.","-");
        //VectorGraphicsEncoder.saveVectorGraphic(chart, "C:\\Users\\julip\\Desktop\\IsysCharts\\" + name, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
        VectorGraphicsEncoder.saveVectorGraphic(chart, "./src/output/" + name, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);

    }

    /***
     * Uses QuickSort for sorting an array
     * @param inputArr the input to be sorted
     */

    private void sort(int[] inputArr) {

        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        quickSort(0, length - 1);
    }


    /***
     * QuickSort implementation. Source: //ToDO insert Source
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
            /**
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

    /***
     * Used by quickSort implementation. change i and j
     * @param i index i to be swapped
     * @param j index j to be swapped
     */
    private void exchangeNumbers(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Stream;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

public class Xchart {
    private int array[];
    private int length;
    Xchart() {

    }

    void simpleChart(DataContainer dataContainer, String name)throws IOException {
        //Group all data, with the day there finished as key
        Map<Integer, List<Integer>> groupedData = dataContainer.getDataContainer().stream()
                                        .collect(groupingBy(Integer::intValue));
        List<Integer> xData = new ArrayList<>();
        List<Integer> yDataPerDay = new ArrayList<>();
        List<Integer> yDataSinceDayOne = new ArrayList<>();
        List<Integer> xtest = new ArrayList();
        xtest.add(1);
        xtest.add(2);
        xtest.add(3);
        xtest.add(5);
        xtest.add(8);
        xtest.add(7);

        List<Integer> ytest = new ArrayList();
        ytest.add(1);
        ytest.add(2);
        ytest.add(2);
        ytest.add(3);
        ytest.add(2);
        ytest.add(4);
        int i = 0;
        //TODO Überprüfen ob es auch mit sorterten maps zu fehlern bei kleinen datenmängen kommt

        for (Map.Entry<Integer, List<Integer>> entry : groupedData.entrySet()) {
            System.out.println(entry.getKey() + "key");
            System.out.println(entry.getValue() + "values");
            xData.add(entry.getKey());
            yDataPerDay.add(entry.getValue().size());
            i += entry.getValue().size();
            yDataSinceDayOne.add(i);
        }

        sort(xData);
        // Create Chart
        /*XYChart chart = QuickChart.getChart("Samples finished", "Past Days", "Samples finished per Day", " ", xData, yDataPerDay);
        saveChart(chart, name,i);
        chart = QuickChart.getChart("Total Samples finished", "Past Days", "Total Samples finished per Time", " ", xData, yDataSinceDayOne);
        saveChart(chart, name,i);*/
        XYChart chart = QuickChart.getChart("Total Samples finished", "Past Days", "Total Samples finished per Time", " ", xtest, ytest);
        saveChart(chart, name,i);
    }

    void saveChart(Chart chart, String name, int i)throws IOException {
        name += "_SampleSize" + i + "_Date" +new Timestamp(System.currentTimeMillis()).toString();
        name = name.replaceAll(":","-");
        name = name.replaceAll("\\.","-");
        VectorGraphicsEncoder.saveVectorGraphic(chart, "C:\\Users\\julip\\Desktop\\IsysCharts\\" + name, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
    }

    public void sort(int[] inputArr) {

        if (inputArr == null || inputArr.length == 0) {
            return;
        }
        this.array = inputArr;
        length = inputArr.length;
        quickSort(0, length - 1);
    }

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

    private void exchangeNumbers(int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

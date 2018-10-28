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
    void simpleChart(DataContainer dataContainer, String name, long sampleSize, double peopleCount){
        //Group all data, with the day there finished as key
        List<Integer> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        boolean roundResult = false;
        if(roundResult) {
            peopleCount = peopleCount - 0.5;
        }
        int[] viewChangesPerDay = dataContainer.getPersonsWithViewChangeTilThisDay();
        boolean firstViewChange = false;
        boolean lastViewChange = false;

        //Adds the information in the x and y data
        for(int day = 0; day < viewChangesPerDay.length; day++) {

            //Is needed to get the first days in with none has view A
            if(!firstViewChange) {
                if(viewChangesPerDay[day] > 0) {
                    firstViewChange = true;
                }
                xData.add(day);
                yData.add((viewChangesPerDay[day] / (double) sampleSize));
                //After the first person has view A
            }else {
                if(viewChangesPerDay[day] > 0) {
                    if(!lastViewChange) {
                        if((viewChangesPerDay[day] / (double) sampleSize) >= peopleCount) {
                            lastViewChange = true;
                            xData.add(day);
                            yData.add((viewChangesPerDay[day] / (double) sampleSize));
                        }else {
                            xData.add(day);
                            yData.add((viewChangesPerDay[day] / (double) sampleSize));
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
    private void saveChart(Chart chart, String name, long sampleSize){
        name += "_SampleSize" + sampleSize + "_Date" +new Timestamp(System.currentTimeMillis()).toString();
        name = name.replaceAll(":","-");
        name = name.replaceAll("\\.","-");

        String os = System.getProperty("os.name").toLowerCase();
        String path = "";

        if(os.contains("windows")){
            path = "src\\output\\";
        }else if(os.contains("nux")) {
            path = "./src/output/";
        }else if(os.contains("mac")) {
            path = "./src/output/";
        }
        try {
            VectorGraphicsEncoder.saveVectorGraphic(chart, path + name, VectorGraphicsEncoder.VectorGraphicsFormat.SVG);
        }catch (IOException e) {
            System.err.println("Cannot save data to " + path +"\nPlease add correct path manually");
        }
    }
}

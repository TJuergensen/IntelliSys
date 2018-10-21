import org.knowm.xchart.*;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class Xchart {
    Xchart() {

    }

    void simpleChart(DataContainer dataContainer) {
        //dataContainer.getDataContainer().stream().sorted().forEach(e -> System.out.println(e));
        Map<Integer, List<Integer>> groupedData = dataContainer.getDataContainer().stream()
                                        .collect(groupingBy(Integer::intValue));
        List<Integer> xData = new ArrayList<>();
        List<Integer> yData = new ArrayList<>();

        for (Map.Entry<Integer, List<Integer>> entry : groupedData.entrySet()) {
           // System.out.println(entry.getKey());
            xData.add(entry.getKey());
            yData.add(entry.getValue().size());
        }

        // Create Chart
        XYChart chart = QuickChart.getChart("Samples finished per Day", "Days", "Finished Samples", "k", xData, yData);

        // Show it
        new SwingWrapper(chart).displayChart();
    }
}

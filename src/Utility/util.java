package Utility;

import logic.Hill;

import java.util.List;
import java.util.function.Function;

/**
 * Utility class for classifying hills
 */
public class util {

    //"Defines"
    private static int characteristicCount = 3; //Amount of used characteristics
    //These are used as kind of enum
    public static final int A = 0;
    public static final int B = 1;
    public static final int UNCERTAIN = 2;
    //"enum" end
    private static int currentCharacteristic = 0; //Keeps track on which characteristic is used right now

    /**
     * Calculates the probability, if a hill is part of an of an already classified List of Hills.
     * @param toClassify The list with the hills the to classified hill is compared to
     * @param avgFromTestSet The Average value of a parameter from the list
     * @param avgFromTestedObject The Average value of the hill that needs to be classified
     * @param getAvgFromHill The function which returns the parameter, which is used for classification
     * @return Returns the Probability to which classification class the tested Object is assignable
     */
    private static double calculateAverage(List<Hill> toClassify, double avgFromTestSet, double avgFromTestedObject, Function<Hill, Double> getAvgFromHill) {
        double count = 0; //this hill is relativlely close to count many hills from the trainingsSet (avg)
        double dif = Math.abs(avgFromTestSet - avgFromTestedObject);
        double diftest;
        for(Hill h : toClassify) {
            diftest = Math.abs(avgFromTestSet - getAvgFromHill.apply(h));

            if(dif < diftest) {
                count++;
            }
        }
        return count/toClassify.size();
    }


    /**
     * Classifies Hills.
     * First it trains with the trainingSets and then classifies the Hill in the toClassify List.
     * All functions for classification are implemented here
     * @param trainingsSetA The TrainingSet which only has Hills that are Classified as A
     * @param trainingsSetB The TrainingSet which only has Hills that are Classified as B
     * @param toClassify The Hills to be classified
     */
    public static void classify(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify) //This method will run all classification-tests
    {
        Function<Hill, Double> relativeHeight = (hill) -> hill.getRelativeHilltopHeight();
        Function<Hill, Double> pointsOnSlope = (hill) ->(double) hill.getPointsOnSlopeCount();
        Function<Hill, Double> height = (hill) -> hill.getAvgHeightHillEndPoints();

        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, relativeHeight);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnSlope);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, height); //kinda good combination with rel-Height and pointsOnSlope
    }

    /**
     * Classifies Hills.
     * First it trains with the trainingSets and than classifies the Hill in the toClassify List.
     * @param trainingsSetA The TrainingSet which only has Hills that are Classified as A
     * @param trainingsSetB The TrainingSet which only has Hills that are Classified as B
     * @param toClassify The Hills to be classified
     * @param getAvgFromHill The Function with which the data trains, and after training classifies the toClassify hills
     */
    private static void classifyWithAvg(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify, Function<Hill,Double> getAvgFromHill) {
        double avgA = trainingsSetA.stream()
                                    .mapToDouble(hill -> getAvgFromHill.apply(hill))
                                    .average()
                                    .getAsDouble();
        double avgB = trainingsSetB.stream()
                                    .mapToDouble(hill -> getAvgFromHill.apply(hill))
                                    .average()
                                    .getAsDouble();
        double isA, isB;
        for (Hill h : toClassify) {
            isA = util.calculateAverage(trainingsSetA, avgA, getAvgFromHill.apply(h), getAvgFromHill);
            h.setProbability(currentCharacteristic, A, isA);
            isB = util.calculateAverage(trainingsSetB, avgB, getAvgFromHill.apply(h), getAvgFromHill);
            h.setProbability(currentCharacteristic, B, isB);

        }
        currentCharacteristic++;
    }

    /**
     * Returns the Characteristic Count
     * @return Returns the Characteristic Count
     */
    public static int getCharacteristicCount() {
        return characteristicCount;
    }
}

package Utility;

import logic.Hill;

import java.util.List;
import java.util.function.Function;

public class util {

    //"Defines"
    public static int characteristicCount = 3;
    //These are used as kind of enum
    public static final int A = 0;
    public static final int B = 1;
    public static final int UNCERTAIN = 2;
    //"enum" end
    public static int curentCharacteristic = 0;

    public static double testAverage(List<Hill> list, double avgFromTestSet, double avgFromTestedObject, Function<Hill, Double> getAvgFromHill) {
        double count = 0; //this hill is relativlely close to count many hills from the trainingsSet (avg)
        double dif = Math.abs(avgFromTestSet - avgFromTestedObject);
        double diftest;
        for(Hill h : list) {
            diftest = Math.abs(avgFromTestSet - getAvgFromHill.apply(h));

            if(dif < diftest) {
                count++;
            }
        }
        return count/list.size();
    }


    public static void classify(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify) //This method will run all classification-tests
    {
        //TODO nu zu double carten wenn es noetig ist
        Function<Hill, Double> relativeHeight = (hill) -> hill.getRelativeHilltopHeight();          //Gutes kreterium
        Function<Hill, Double> tilt = (hill) -> hill.getAvgTilt();                                  //Nicht geeignet
        Function<Hill, Double> pointsOnHilltop = (hill) ->(double) hill.getPointsOnHilltopCount();  //Nicht gut zum Klassifizieren
        Function<Hill, Double> pointsOnSlope = (hill) ->(double) hill.getPointsOnSlopeCount();      //Gutes kreterium, aber praktisch relativeheight ?
        Function<Hill, Double> pointsAfterSlope = (hill) ->(double) hill.getPointsAfterSlopeCount();//Nicht gut zum Klassifizieren
        Function<Hill, Double> hilltopHeight = (hill) ->(double) hill.getHilltopHeight();           //Kann vieleicht genutzt werden
        Function<Hill, Double> height = (hill) ->(double) hill.getAvgHeightHillEndPoints();           //Kann man nutzen, aber eigetnlich kombination aus hilltopHeight - relativeHeight und
        Function<Hill, Double> maxHilltopShift = (hill) ->(double) hill.getMaxHilltopShift();       //Nicht geeignet //TODO für shift ende ende der slope betrachten

        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, relativeHeight);
        //classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, tilt); //absolute garbage
        //classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnHilltop); //absolute garbage
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnSlope);
        //classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsAfterSlope); //absolute garbage
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, height); //kinda good combination with rel-Height and pointsOnSlope
        //classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, hilltopHeight); // not good
        //classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, maxHilltopShift); // forget it
    }


    public static void classifyWithAvg(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify, Function<Hill,Double> getAvgFromHill) {
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
            isA = util.testAverage(trainingsSetA, avgA, getAvgFromHill.apply(h), getAvgFromHill);
            h.setProbability(curentCharacteristic, A, isA);
            isB = util.testAverage(trainingsSetB, avgB, getAvgFromHill.apply(h), getAvgFromHill);
            h.setProbability(curentCharacteristic, B, isB);

        }
        curentCharacteristic++;
    }
}

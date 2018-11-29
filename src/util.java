import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class util {

    //"Defines"
    public static int characteristicCount = 8;
    //These are used as kind of enum
    public static final int A = 0;
    public static final int B = 1;
    public static final int UNCERTAIN = 2;
    public static int curentCharacteristic = 0;
    //"enum" end
    public static final double UNCERTAINITYRANGE[] = {0.2, 0.1, 0.1, 0.1}; //Range in which A and B need to be to be uncertain if it is A or B. WITHIN this range = uncertain


    public static double testAverage(List<Hill> list, double avgFromTestSet, double avgFromTestedObject, Function<Hill, Double> getAvgFromHill) {
        double count = 0; //this hill is relativlely close to this many hills from the trainingsSet (avg)
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
        Function<Hill, Double> relativeHeight = (hill) -> hill.getRelativeHilltopHeight();          //Gutes kreterium
        Function<Hill, Double> tilt = (hill) -> hill.getAvgTilt();                                  //Nicht geeignet
        Function<Hill, Double> pointsOnHilltop = (hill) ->(double) hill.getPointsOnHilltopCount();  //Nicht gut zum Klassifizieren
        Function<Hill, Double> pointsOnSlope = (hill) ->(double) hill.getPointsOnSlopeCount();      //Gutes kreterium, aber praktisch relativeheight ?
        Function<Hill, Double> pointsAfterSlope = (hill) ->(double) hill.getPointsAfterSlopeCount();//Nicht gut zum Klassifizieren
        Function<Hill, Double> hilltopHeight = (hill) ->(double) hill.getHilltopHeight();           //Kann vieleicht genutzt werden
        Function<Hill, Double> height = (hill) ->(double) hill.getAvgHeightHillEndPoints();           //Kann man nutzen, aber eigetnlich kombination aus hilltopHeight - relativeHeight und
        Function<Hill, Double> maxHilltopShift = (hill) ->(double) hill.getMaxHilltopShift();       //Nicht geeignet //TODO für shift ende ende der slope betrachten

        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, relativeHeight);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, tilt);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnHilltop);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnSlope);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsAfterSlope);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, height);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, hilltopHeight);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, maxHilltopShift);
    }


    public static void classifyWithAvg(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify, Function<Hill,Double> getAvgFromHill) {
        double avgA = trainingsSetA.stream()
                                    .mapToDouble(hill -> getAvgFromHill.apply(hill))
                                    .average()
                                    .getAsDouble();
        System.out.println("A: " + avgA);
        double avgB = trainingsSetB.stream()
                                    .mapToDouble(hill -> getAvgFromHill.apply(hill))
                                    .average()
                                    .getAsDouble();
        System.out.println("B: " + avgB);
        double isA, isB;
        int countA = 0;
        int countB = 0;
        int unsicher = 0;

        for (Hill h : toClassify) { //ToDO Change trainingsSetA to toClassify
            isA = util.testAverage(trainingsSetA, avgA, getAvgFromHill.apply(h), getAvgFromHill);
            //System.out.println(getAvgFromHill.apply(h));
            h.setProbability(curentCharacteristic, A, isA);
            System.out.println("isA: " + isA + "%");
            isB = util.testAverage(trainingsSetB, avgB, getAvgFromHill.apply(h), getAvgFromHill);
            h.setProbability(curentCharacteristic, B, isB);
            System.out.println("isB: " + isB + "%");
            //System.out.println("\n\n\n");

            //ToDO REMOVE!  This is obselete and will be done in the hill object itself
            double dif = isA - isB;
            if (dif < 0) {
                dif = dif * -1;
            }
            //System.out.println(dif);
            if (dif > 0.0) {
                if (isA > isB) {
                    countA++;
                } else {
                    countB++;
                }
            } else {
                unsicher++;
            }
            //ToDO END of removal
        }


        //ToDO move this to a Print method which runs through each tested Hill
        System.out.println("Als A erkannt: " + countA);
        System.out.println("Als B erkannt: " + countB);
        System.out.println("Nicht sicher : " + unsicher);
        curentCharacteristic++;
    }
}

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class util {

    //"Defines"
    public static int characteristicCount = 5;
    //These are used as kind of enum
    public static final int A = 0;
    public static final int B = 1;
    public static final int UNCERTAIN = 2;
    public static int curentCharacteristic = 0;
    //"enum" end
    public static final double UNCERTAINITYRANGE = 0.6; //Range in which A and B need to be to be uncertain if it is A or B. WITHIN this range = uncertain


    public static double testAverage(List<Hill> list, double avgFromTestSet, double avgFromTestedObject) {
        double count = 0; //this hill is relativlely close to this many hills from the trainingsSet (avg)
        double dif = Math.abs(avgFromTestSet - avgFromTestedObject);
        double diftest;
        for(Hill h : list) {
            diftest = Math.abs(avgFromTestSet - h.getRelativeHilltopHeight());

            if(dif < diftest) {
                count++;
            }
        }
        return count/list.size();
    }


    public static void classify(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify) //This method will run all classification-tests
    {
        Function<Hill, Double> relativeHeight = (hill) -> hill.getRelativeHilltopHeight();
        Function<Hill, Double> relativeTilt = (hill) -> hill.getAvgTilt();
        Function<Hill, Double> pointsOnHilltop = (hill) ->(double) hill.getPointsOnHilltopCount();
        Function<Hill, Double> pointsOnSlope = (hill) ->(double) hill.getPointsOnSlopeCount();
        Function<Hill, Double> pointsAfterSlope = (hill) ->(double) hill.getPointsAfterSlopeCount();

        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, relativeHeight);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, relativeTilt);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnHilltop);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsOnSlope);
        classifyWithAvg(trainingsSetA, trainingsSetB, toClassify, pointsAfterSlope);
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

        for (Hill h : trainingsSetA) { //ToDO Change trainingsSetA to toClassify
            isA = util.testAverage(trainingsSetA, avgA, getAvgFromHill.apply(h));
            //System.out.println(getAvgFromHill.apply(h));
            h.setProbability(0, A, isA);
            //System.out.println("isA: " + isA + "%");
            isB = util.testAverage(trainingsSetB, avgB, getAvgFromHill.apply(h));
            h.setProbability(0, B, isB);
            //System.out.println("isB: " + isB + "%");
            //System.out.println("\n\n\n");

            //ToDO REMOVE!  This is obselete and will be done in the hill object itself
            double dif = isA - isB;
            if (dif < 0) {
                dif = dif * -1;
            }
            if ((isA > 0.5 || isB > 0.5) && dif > 0.3) {
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

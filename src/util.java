import java.util.List;

public class util {

    //"Defines"
    public static int characteristicCount = 1;
    //These are used as kind of enum
    public static final int A = 0;
    public static final int B = 1;
    public static final int UNCERTAIN = 2;
    //"enum" end
    public static final double UNCERTAINITYRANGE = 0.6; //Range in which A and B need to be to be uncertain if it is A or B. WITHIN this range = uncertain


    public static double calculateAvgHeight(List<Hill> list) {
        double ret = 0.0;
        for (Hill h : list) {
            ret += h.getRelativeHilltopHeight();
        }
        return ret/list.size();
    }

    public static double testAverage(List<Hill> list, double avgFromTestSet, double avgFromTestedObject) {
        double count = 0;
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

        classifyWithAvgHeight(trainingsSetA, trainingsSetB, toClassify);
    }

    public static void classifyWithAvgHeight(List<Hill> trainingsSetA, List<Hill> trainingsSetB, List<Hill> toClassify) {
        double avgA = calculateAvgHeight(trainingsSetA);
        System.out.println("A: " + avgA);
        double avgB = calculateAvgHeight(trainingsSetB);
        System.out.println("B: " + avgB);
        double isA, isB;
        int countA = 0;
        int countB = 0;
        int unsicher = 0;


        for (Hill h : trainingsSetA) { //ToDO Change trainingsSetA to toClassify
            isA = util.testAverage(trainingsSetA, avgA, h.getRelativeHilltopHeight());
            System.out.println(h.getRelativeHilltopHeight());
            h.setProbability(0, A, isA);
            System.out.println("isA: " + isA + "%");

            isB = util.testAverage(trainingsSetB, avgB, h.getRelativeHilltopHeight());
            h.setProbability(0, B, isB);
            System.out.println("isB: " + isB + "%");
            System.out.println("\n\n\n");

            //ToDO REMOVE!  This is obselete and will be done in the hill object itself
            double dif = isA - isB;
            if (dif < 0) {
                dif = dif * -1;
            }
            if ((isA > 0.5 || isB > 0.5) && dif > 0.6) {
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
    }

}

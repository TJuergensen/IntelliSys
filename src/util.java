import java.util.List;

public class util {

    //"Defines"
    public static int characteristicCount = 1;
    //These are used as kind of enum
    public static final int A = 0;
    public static final int B = 1;
    public static final int UNCERTAIN = 2;
    //"enum" end


    public static double calculateAvgHeight(List<Hill> list) {
        double ret = 0.0;
        for (Hill h : list) {
            ret += h.getRelativeHilltopHeight();
        }
        return ret/list.size();
    }

    public static double test(List<Hill> list, double avg, double a) {
        double count = 0;
        double dif = avg - a;
        if(dif < 0) {
            dif = dif * -1.0;
        }
        double diftest;
        for(Hill h : list) {
            diftest = avg - h.getRelativeHilltopHeight();
            if(diftest < 0) {
                diftest = diftest * -1.0;
            }
            if(dif < diftest) {
                count++;
            }
        }
        return count/list.size();
    }

}

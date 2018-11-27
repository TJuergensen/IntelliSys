import java.util.List;

public class util {

    public static double calculateAvgHeight(List<Hill> list) {
        double ret = 0.0;
        for(Hill h : list){
            ret += h.getRelativeHilltopHeight(); }
        return ret/list.size();
    }
}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static String savePath  = "src\\output\\hilldetectiontest.png";
    static String aPath     = "src\\input\\A0.csv";
    static String bPath     = "src\\input\\B0.csv";
    static String dataPath  = "src\\input\\data.csv";

    public static void main(String[] args) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        //Check if os is linux
        if(os.contains("nux")) {
            savePath  = "hilldetectiontest.png";
            aPath     = "A0.csv";
            bPath     = "B0.csv";
            dataPath  = "data.csv";
        }
        //load Data
        String[][] data = loadDATA();
        final boolean loadA = true; //Is only used to decide if A or B needs to be loaded
        ArrayList<Hill> hillsB = load(data, !loadA);
        ArrayList<Hill> hillsA = load(data, loadA);

        //Print image
        //printData(data);

        double avgA = util.calculateAvgHeight(hillsA);
        System.out.println("A: " + avgA);
        double avgB = util.calculateAvgHeight(hillsB);
        System.out.println("B: " + avgB);
        double isA, isB;
        int countA = 0;
        int countB = 0;
        int unsicher = 0;
        for(Hill h : hillsA) {
            isA = util.test(hillsA, avgA, h.getRelativeHilltopHeight());
            System.out.println(h.getRelativeHilltopHeight());
            System.out.println("isA: " + isA + "%");
            isB = util.test(hillsB, avgB, h.getRelativeHilltopHeight());
            System.out.println("isB: " + isB + "%");
            System.out.println("\n\n\n");

            double dif = isA - isB;
            if(dif < 0) {
                dif = dif * -1;
            }
            if((isA > 0.5 || isB > 0.5) && dif > 0.6) {
                if (isA > isB) {
                    countA++;
                } else {
                    countB++;
                }
            }else {
                unsicher++;
            }
        }
        System.out.println("Als A erkannt: " + countA);
        System.out.println("Als B erkannt: " + countB);
        System.out.println("Nicht sicher : " + unsicher);

    }

    private static void printData(String[][] data) throws IOException {
        File file = new File(savePath);
        int w = data.length;
        int h = data[0].length;
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage image = new BufferedImage(w, h, type);

        Double a;
        int b;
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                a = Double.parseDouble(data[x][y]);

                //the amount of zeros represents the decimal precision
                b = (int)Math.round(a*10000);
                image.setRGB(x, y, b);
            }
        }
        ImageIO.write(image, "PNG", file);
    }

    private static ArrayList<Hill> load(String[][] data, boolean loadA) throws IOException {
        String path;
        if(loadA) {
            path = aPath;
        }else {
            path = bPath;
        }
        Scanner scanner = new Scanner(new File(path));
        scanner.useDelimiter(",");
        ArrayList<Hill> hills = new ArrayList();
        String dummy;
        String[] k;
        String x = "";
        String y;
        while(scanner.hasNext()){
            dummy = scanner.next();
            if(dummy.contains("\n")) {
                k = dummy.split("\\r?\\n");
                y = k[0];
                hills.add(new Hill(data, Integer.parseInt(x), Integer.parseInt(y), loadA));
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scanner.close();
        return hills;
    }

    private static String[][] loadDATA() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(dataPath));
        scanner.useDelimiter(",");
        int row= 0;
        int col= 0;
        String dummy;
        String[] k;
        String[][] data = new String[3000][4943];
        while(scanner.hasNext()){
            dummy = scanner.next();
            if(dummy.contains("\n")) {
                k = dummy.split("\\r?\\n");
                data[row++][col++] = k[0];
                row=0;
                if(k.length == 2) {
                    data[row++][col] = k[1];
                }
            } else {
                data[row++][col] = dummy;
            }
        }
        scanner.close();
        return data;
    }
}

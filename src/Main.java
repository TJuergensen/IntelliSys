import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    static String savePath  = "src\\output\\hilldetectiontest.png";
    static String trainingsSetA_path     = "src\\input\\A0.csv";
    static String trainingsSetB_path     = "src\\input\\B0.csv";
    static String toClassify_path  = "";
    static String dataPath  = "src\\input\\data.csv";
    static ArrayList<Hill> trainingsSet_B, trainingsSet_A, toClassify;

    public static void main(String[] args) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        //Check if os is linux
        if(os.contains("nux")) {
            savePath  = "hilldetectiontest.png";
            trainingsSetA_path = "A0.csv";
            trainingsSetB_path = "B0.csv";
            toClassify_path =" ";
            dataPath  = "data.csv";
        }
        //load Data
        String[][] data = loadDATA();
        load(data);
        
        //Print image
        //printData(data);
        util.classify(trainingsSet_A, trainingsSet_B, null);

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

    private static void load(String[][] data) throws IOException {
        trainingsSet_A = new ArrayList<Hill>();
        trainingsSet_B = new ArrayList<Hill>();
        toClassify = new ArrayList<Hill>();


        Scanner scannerTSA = new Scanner(new File(trainingsSetA_path));
        scannerTSA.useDelimiter(",");
               
        String dummy;
        String[] k;
        String x = "";
        String y;
        while(scannerTSA.hasNext()){
            dummy = scannerTSA.next();
            if(dummy.contains("\n")) {
                k = dummy.split("\\r?\\n");
                y = k[0];
                trainingsSet_A.add(new Hill(data, Integer.parseInt(x), Integer.parseInt(y), true));
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scannerTSA.close();

        Scanner scannerTSB = new Scanner(new File(trainingsSetB_path));
        scannerTSB.useDelimiter(",");

        while(scannerTSB.hasNext()){
            dummy = scannerTSB.next();
            if(dummy.contains("\n")) {
                k = dummy.split("\\r?\\n");
                y = k[0];
                trainingsSet_B.add(new Hill(data, Integer.parseInt(x), Integer.parseInt(y), false));
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scannerTSB.close();

        Scanner scannerTC = new Scanner(new File(toClassify_path));
        scannerTC.useDelimiter(",");

        while(scannerTC.hasNext()){
            dummy = scannerTC.next();
            if(dummy.contains("\n")) {
                k = dummy.split("\\r?\\n");
                y = k[0];
                toClassify.add(new Hill(data, Integer.parseInt(x), Integer.parseInt(y), false));
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scannerTC.close();
        
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

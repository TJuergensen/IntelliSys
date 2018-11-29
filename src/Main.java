import Utility.util;
import logic.Hill;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Is used to Classifies hills. Trains automatically.
 * For more Information visit https://lernraum.th-luebeck.de/mod/assign/view.php?id=96767
 */
public class Main {
    private static String savePath  = "src\\output\\hilldetectiontest.png";
    private static String trainingsSetA_path     = "src\\input\\A0.csv";
    private static String trainingsSetB_path     = "src\\input\\B0.csv";
    private static String toClassify_path  = "src\\input\\A1.csv";
    private static String dataPath  = "src\\input\\data.csv";

    private static ArrayList<Hill> trainingsSet_B = new ArrayList<>();
    private static ArrayList<Hill> trainingsSet_A = new ArrayList<>();
    private static ArrayList<Hill> toClassify = new ArrayList<>();

    /**
     * Is used to Classifies hills. Trains automatically.
     * For more Information visit https://lernraum.th-luebeck.de/mod/assign/view.php?id=96767
     * @param args //Test Params that aren't used
     * @throws IOException Throws an IOException while reading a file
     */
    public static void main(String[] args) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        //Check if os is linux
        if(os.contains("nux")) {
            savePath  = "hilldetectiontest.png";
            trainingsSetA_path = "A0.csv";
            trainingsSetB_path = "B0.csv";
            toClassify_path ="B1.csv";
            dataPath  = "data.csv";
        }
        //load Data
        String[][] data = loadDATA();
        load(data, trainingsSet_A, trainingsSetA_path, util.A);
        load(data, trainingsSet_B, trainingsSetB_path, util.B);
        load(data, toClassify, toClassify_path, util.UNCERTAIN);

        if(Hill.isPrintImageEnabled()) printData(data);


        util.classify(trainingsSet_A, trainingsSet_B, toClassify);
        int countA=0;
        int countB=0;
        int countUncertain=0;
        for(Hill h: toClassify)
        {

            switch (h.calculateObjectType()) {
                case util.A:
                    countA++;
                    break;
                case util.B:
                    countB++;
                    break;
                case util.UNCERTAIN:
                    countUncertain++;
                    break;
            }
        }

        System.out.println("A: " + countA + "\nB: "+countB +"\nUnsicher: "+countUncertain);
    }

    /**
     * Creates an Image from the data in which the hills are marked
     * @param data The data which will be converted in to a PNG
     * @throws IOException Throws an IOException while reading a file
     */
    private static void printData(String[][] data) throws IOException {
        File file = new File(savePath);
        int w = data.length;
        int h = data[0].length;
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage image = new BufferedImage(w, h, type);
        double a;
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

    /**
     * Loads the position of the hills out of an csv file
     * @param data The data in which the hill is located
     * @param hills List in which the new Hills will be saved
     * @param path  The File Path where the csv is located
     * @param classification Declares if the Hills are from Type A or B or still undefined
     * @throws IOException Throws an IOException while reading a file
     */
    private static void load(String[][] data, List<Hill> hills, String path, int classification) throws IOException{
        Scanner scanner = new Scanner(new File(path));
        scanner.useDelimiter(",");

        String dummy;
        String[] k;
        String x = "";
        String y;
        while(scanner.hasNext()){
            dummy = scanner.next();
            if(dummy.contains("\n")) {
                k = dummy.split("\\r?\\n");
                y = k[0];
                hills.add(new Hill(data, Integer.parseInt(x), Integer.parseInt(y), classification));
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scanner.close();

    }

    /**
     * Loads and saves the data in which the hills are located
     * @return Returns the loaded data
     * @throws FileNotFoundException Throws an IOException while reading a file
     */
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

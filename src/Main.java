import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static String savePath  = "src\\output\\hilldetectiontest.png";
    static String a0Path    = "src\\input\\A0.csv";
    static String b0Path    = "src\\input\\B0.csv";
    static String dataPath  = "src\\input\\data.csv";

    public static void main(String[] args) throws IOException {
        File f = new File(savePath);
        String[][] data = loadDATA();
        data = loadB0(data);
        data = loadA0(data);
        int w = data.length;
        int h = data[0].length;
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage image = new BufferedImage(w, h, type);

        Double a;
        int b;
        for(int y = 0; y < h; y++) {
            for(int x = 0; x < w; x++) {
                a = Double.parseDouble(data[x][y]);
                b = (int)Math.round(a*10000);
                image.setRGB(x, y, b);
            }
        }
        ImageIO.write(image, "PNG", f);
    }

    private static String[][] loadA0(String[][] data) throws IOException {
        Scanner scanner = new Scanner(new File(a0Path));
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
                //data[Integer.parseInt(x)][Integer.parseInt(y)] = color;
                //data = fill(data, Integer.parseInt(x), Integer.parseInt(y), color);
                new Hill(data, Integer.parseInt(x), Integer.parseInt(y), false);
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scanner.close();
        return data;
    }
    private static String[][] loadB0(String[][] data) throws IOException {
        Scanner scanner = new Scanner(new File(b0Path));
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
                //data[Integer.parseInt(x)][Integer.parseInt(y)] = color;
                //data = fill(data, Integer.parseInt(x), Integer.parseInt(y), color);
                new Hill(data, Integer.parseInt(x), Integer.parseInt(y), true);
                if(k.length == 2) {
                    x = k[1];
                }
            } else {
                x = dummy;
            }
        }
        scanner.close();
        return data;
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
/*
    private static String[][] fill(String[][] data, int x,int y, String color) {
        data[x-1][y-1] = color;
        data[x][y-1] = color;
        data[x+1][y-1] = color;

        data[x-1][y] = color;
        data[x][y] = color;
        data[x+1][y] = color;

        data[x-1][y+1] = color;
        data[x][y+1] = color;
        data[x+1][y+1] = color;
        return data;
    }
    */
}

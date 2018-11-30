package logic;

import Utility.util;
import java.util.ArrayList;

/**
 * Represents a hill in the data. Finds and calculates all of the hill properties.
 */
public class Hill {

    private int classification; //Is used to store if the hill is A or B or undefined

    /*
    printImage
    IMPORTANT!!! if true, it changes the the values in the data and the classification is turned off.
    For classification this needs to be false
     */
    private static boolean printImage = false;
    private final String colorA = "16776960";           //Yellow
    private final String colorB = "16711680";           //RED
    private final String colorUndefined = "2287";       //Blue
    private String colorTop;
    private final String colorEndOfSlope = "0";
    private final String colorSlope = "16777215";

    //Variables to play with
    private final int maxX = 3000;  //The horizontel max of our data array
    private final int maxY = 4943;  //The vertical max of our data array

    //If the slope tilt falls under this value, its the end of the slope
    private final double minAvgHeightDifference = 0.3;
    //The max difference to our hilltopHeight, which indicates where the hilltop is located
    private final double maxDifferenceToHilltop = 2.0;
    private String[][] data;    //The Reference to the data array where our hill is located

    //Structures to save all data
    private double hilltopHeight; //The height value of the hill from the start position
    //The height of all the endpoints from the hilldetection
    private ArrayList<Double> endOfHillHeights = new ArrayList();
    private double minHeightHillEndPoint;
    private double avgHeightHillEndPoints;
    private int pointsOnSlopeCount = 0;     //The lenght of the slope in our data
    private double relativeHilltopHeight;   //The relative height of the hill compared to it surroundings
    private double[][] probabilityList;     //Stores probabilities. [][0] probability for A, [][1] probability for B

    /**
     * Creates a Hill and finds and/or calculates all properties of the hill
     * @param data A reference to the data array in which the hill is placed
     * @param x The horizontal start position of a hill in the data
     * @param y The vertical start position of a hill in the data
     * @param classif Should be A or B if the Hill is A or B and undefined if not classified yet.
     */
    public Hill(String[][] data, int x, int y, int classif) {
        this.classification = classif;
        this.data = data;
        this.hilltopHeight = Double.parseDouble(data[x][y]);
        markHill(x, y);
        calculate();
        probabilityList = new double[util.getCharacteristicCount()][2];
    }

    /**
     * Saves the propabillity for A or B for a specific characteristic
     * @param characteristic The Characteristic for which the probability is saved
     * @param AorB Represents if the value is for A or B
     * @param value The probability value that needs to be saved
     */
    public void setProbability(int characteristic, int AorB, double value) {
        probabilityList[characteristic][AorB] = value;
    }

    /**
     * Classifies the hill as A or B or undefined.
     * @return Returns the int representation from A or B or undefined
     */
    public int calculateObjectType() {
        int ret;

        //Start with 100%; multiplication will lower these values
        double probabilityA =1.0;
        double probabilityB =1.0;
        //count probabilities
        for(int i=0; i<util.getCharacteristicCount(); i++){
            probabilityA *= probabilityList[i][util.A];
            probabilityB *= probabilityList[i][util.B];
        }

        //Check which is greater. == should be NEARLY impossible. If equals, count for B. Happens with the used  characteristics.
        if (probabilityA > probabilityB) {
            ret = util.A;
        } else {
            ret = util.B;
        }

        return (classification=ret);
    }

    /**
     * calls calculation methods for the hill data. Essentially needed for classification
     */
    private void calculate() {
        calculateAvgHeightBetweenBottomAndTop();
        calculateMinHeightBetweenBottomAndTop();
        relativeHilltopHeight = hilltopHeight - minHeightHillEndPoint;
    }

    /**
     * Calculates the minimum height between the bottom of the hill and its top
     */
    private void  calculateMinHeightBetweenBottomAndTop() {
        double min = 1000000000;

        for(double i : endOfHillHeights) {
            if(i < min) {
                min = i;
            }
        }
        this.minHeightHillEndPoint = min;
    }

    /**
     * Calculates the average height between all bottom positions and the hilltop
     */
    private void calculateAvgHeightBetweenBottomAndTop() {
        this.avgHeightHillEndPoints = endOfHillHeights.stream().mapToDouble(d -> d).average().getAsDouble();
    }

    /**
     * Calls markHill(int x, int y, int xOffset, int yOffset, double oldPointOnHill, int distance) in every nessasary
     * direction. Sets the color for the top of the hill.
     * @param x The horizontal start position of a hill in the data
     * @param y The vertical start position of a hill in the data
     */
    private void markHill(int x, int y) {

        switch (classification){
            case util.A:
                this.colorTop= colorA;
                    break;
            case util.B:
                this.colorTop= colorB;
                break;
            case util.UNCERTAIN:
                this.colorTop= colorUndefined;
                break;
        }

        int initDistance = 0;

        // mark hill in every direction
        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(!(i == 0 && j == 0)) {
                    markHill(x, y, i, j, this.hilltopHeight, initDistance);
                }
            }
        }
    }

    /**
     * Goes in one direction of the hill, marks the hill if desired and saves all information
     * from the hill in the process.
     * @param x The horizontal position in the data
     * @param y The vertical position in the data
     * @param xOffset The horizontal offset for x
     * @param yOffset The vertical offset for y
     * @param oldPointOnHill The height of the old position
     * @param distance The distance the hill was market till now
     */
    private void markHill(int x, int y, int xOffset, int yOffset, double oldPointOnHill, int distance) {
        //If the new Position in still in the data array do...
        y = y + yOffset;
        x = x + xOffset;
        if(y < this.maxY && y > 0 && x < this.maxX && x > 0) {
            double newPointOnHill = Double.parseDouble(data[x][y]);

            //If the next field is already part of another hill
            if(newPointOnHill == Double.parseDouble(colorEndOfSlope)) {
                endOfHillHeights.add(oldPointOnHill);
                return;
            }
            //Recognize if its still the part of the hilltop and mark it as such
            if(newPointOnHill < this.hilltopHeight + this.maxDifferenceToHilltop && newPointOnHill > this.hilltopHeight - this.maxDifferenceToHilltop){ //Still on Plateau?
               if(printImage) {data[x][y] = this.colorTop;}
                markHill(x, y, xOffset, yOffset, newPointOnHill, ++distance); //Recursion for this direction
                return;
            }else {
                //Recognize if the new position is part of the slope
                if (newPointOnHill < oldPointOnHill) {
                    double tilt = (oldPointOnHill - newPointOnHill) / 2;
                    //Recognize if its still steep enough
                    if (tilt < this.minAvgHeightDifference) {
                        if(printImage) {this.data[x][y] = this.colorEndOfSlope;}
                        markHill(x, y, xOffset, yOffset, newPointOnHill, ++distance); //Recursion for steepness
                        return;
                    } else {
                        pointsOnSlopeCount++;
                        if(printImage) {this.data[x][y] = this.colorSlope;}
                        markHill(x, y, xOffset, yOffset, newPointOnHill, ++distance);
                        return;
                    }
                }
            }
        }
        endOfHillHeights.add(oldPointOnHill);
    }


    /**
     * Returns the average height of the hill ending points
     * @return Returns the avgHeightHillEndPoints
     */
    public double getAvgHeightHillEndPoints() {
        return avgHeightHillEndPoints;
    }

    /**
     * Returns the pointsOnSlopeCount
     * @return Returns the pointsOnSlopeCount
     */
    public int getPointsOnSlopeCount() {
        return pointsOnSlopeCount;
    }

    /**
     * Returns the relativeHillTopHeight
     * @return Returns the relativeHillTopHeight
     */
    public double getRelativeHilltopHeight() {
        return relativeHilltopHeight;
    }

    /**
     * Returns if printing is Enabled ?
     * @return Returns if printing is enabled
     */
    public static boolean isPrintImageEnabled() {
        return printImage;
    }
}

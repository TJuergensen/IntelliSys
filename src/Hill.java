import java.util.ArrayList;

class Hill {
    private boolean isA;
    private final String colorA = "16776960";
    private final String colorB = "16711680";
    private String colorTop;
    private final String colorEndOfSlope = "0";
    private final String colorSlope = "16777215";
    private double hilltopHeight;
    private final int maxX = 3000;
    private final int maxY = 4943;
    private final double minAvgHeightDifference = 1.1;
    private final double maxDifferenceToHilltop = 2.0;
    private String[][] data;

    //Structures to save all data
    private ArrayList<Double> endOfHillHeights = new ArrayList();
    private double minHeightHillEndPoint;
    private double maxHeightHillEndPoint;
    private double avgHeightHillEndPoints;

    private int pointsOnHilltopCount = 0;
    private int pointsOnSlopeCount = 0;
    private int pointsAfterSlopeCount = 0;

    private ArrayList<Integer> distancesHilltopAndEndOfHill = new ArrayList<>();
    private double avgDistanceHilltopAndEndOfHill;

    private ArrayList<Double> tilts = new ArrayList();
    private double avgTilt;

    private double relativeHilltopHeight;

    Hill(String[][] data, int x, int y, boolean isA) {
        this.isA = isA;
        this.data = data;
        this.hilltopHeight = Double.parseDouble(data[x][y]);
        markHill(x, y);
        calculate();

        System.out.println(
        "\nisA                              : " + isA +
        "\nminHeightHillEndPoint            : " + minHeightHillEndPoint +
        "\nmaxHeightHillEndPoint            : " + maxHeightHillEndPoint +
        "\navgHeightHillEndPoints           : " + avgHeightHillEndPoints +
        "\npointsOnHilltopCount             : " + pointsOnHilltopCount +
        "\npointsOnSlopeCount               : " + pointsOnSlopeCount +
        "\npointsAfterSlopeCount            : " + pointsAfterSlopeCount +
        "\navgDistanceHilltopAndEndOfHill   : " + avgDistanceHilltopAndEndOfHill +
        "\navgTilt                          : " + avgTilt +
        "\nrelativeHeightOfHill             : " + relativeHilltopHeight +
        "\nheightOfHill                     : " + hilltopHeight
        );
    }

    private void calculate() {
        calculateAvgDistanceHilltopAndEndOfHill();
        calculateAvgHeightBetweenBottomAndTop();
        calculateMinAndMaxHeightBetweenBottomAndTop();
        calculateAvgTilt();
        relativeHilltopHeight = hilltopHeight - minHeightHillEndPoint;
    }

    private void calculateAvgTilt() {
        this.avgTilt = this.tilts.stream().mapToDouble(d -> d).average().getAsDouble();
    }

    private void  calculateMinAndMaxHeightBetweenBottomAndTop() {
        double min = 1000000000;
        double max = 0;

        for(double i : endOfHillHeights) {
            if(i < min) {
                min = i;
            }
            if(i > max) {
                max = i;
            }
        }
        this.minHeightHillEndPoint = min;
        this.maxHeightHillEndPoint = max;
    }

    private void calculateAvgDistanceHilltopAndEndOfHill() {
        this.avgDistanceHilltopAndEndOfHill = distancesHilltopAndEndOfHill.stream().mapToDouble(d -> d).average().getAsDouble();
    }

    private void calculateAvgHeightBetweenBottomAndTop() {
        this.avgHeightHillEndPoints = endOfHillHeights.stream().mapToDouble(d -> d).average().getAsDouble();
    }

    private void markHill(int x, int y) {
        if(isA) {
            this.colorTop= colorA;
        }else {
            this.colorTop= colorB;
        }
        final int stay = 0;
        final int goRight = 1;
        final int goLeft = -1;
        final int goUp = 1;
        final int goDown = -1;

        int initDistance = 0;
        //right
        markHill(x, y, goRight, stay, this.hilltopHeight, initDistance);
        //left
        markHill(x, y, goLeft,  stay, this.hilltopHeight, initDistance);
        //up
        markHill(x, y, stay,    goUp, this.hilltopHeight, initDistance);
        //down
        markHill(x, y, stay,    goDown, this.hilltopHeight, initDistance);
        //up left
        markHill(x, y, goLeft,  goUp, this.hilltopHeight, initDistance);
        //up right
        markHill(x, y, goRight, goUp, this.hilltopHeight, initDistance);
        //down left
        markHill(x, y, goLeft,  goDown, this.hilltopHeight, initDistance);
        //down right
        markHill(x, y, goRight, goDown, this.hilltopHeight, initDistance);

    }

    private boolean markHill(int x, int y, int xOffset, int yOffset, double oldPointOnHill, int distance) {
        //If the new Position in still in the data array do...
        y = y + yOffset;
        x = x + xOffset;
        if(y < this.maxY && y > 0 && x < this.maxX && x > 0) {
            double newPointOnHill = Double.parseDouble(data[x][y]);

            //If the next field is allready part of another hill
            if(newPointOnHill == Double.parseDouble(colorEndOfSlope)) {
                distancesHilltopAndEndOfHill.add(distance);
                endOfHillHeights.add(oldPointOnHill);
                return false;
            }
            //Recognise if its still the part of the hilltop and mark it as such
            if(newPointOnHill < this.hilltopHeight + this.maxDifferenceToHilltop && newPointOnHill > this.hilltopHeight - this.maxDifferenceToHilltop){
                data[x][y] = this.colorTop;
                pointsOnHilltopCount++;
                return markHill(x, y, xOffset, yOffset, newPointOnHill, ++distance);
            }else {
                //Recognise if the new position is part of the slope
                if (newPointOnHill < oldPointOnHill) {
                    Double tilt = (oldPointOnHill - newPointOnHill) / 2;
                    tilts.add(tilt);
                    //Recognize if its still steep enough
                    if (tilt < this.minAvgHeightDifference) {
                        this.data[x][y] = this.colorEndOfSlope;
                        pointsAfterSlopeCount++;
                        return markHill(x, y, xOffset, yOffset, newPointOnHill, ++distance);
                    } else {
                        pointsOnSlopeCount++;
                        this.data[x][y] = this.colorSlope;
                        return markHill(x, y, xOffset, yOffset, newPointOnHill, ++distance);
                    }
                }
            }
        }
        distancesHilltopAndEndOfHill.add(distance);
        endOfHillHeights.add(oldPointOnHill);
        return false;
    }

    //getter

    public ArrayList<Double> getEndOfHillHeights() {
        return endOfHillHeights;
    }

    public double getMinHeightHillEndPoint() {
        return minHeightHillEndPoint;
    }

    public double getMaxHeightHillEndPoint() {
        return maxHeightHillEndPoint;
    }

    public double getAvgHeightHillEndPoints() {
        return avgHeightHillEndPoints;
    }

    public int getPointsOnHilltopCount() {
        return pointsOnHilltopCount;
    }

    public int getPointsOnSlopeCount() {
        return pointsOnSlopeCount;
    }

    public int getPointsAfterSlopeCount() {
        return pointsAfterSlopeCount;
    }

    public ArrayList<Integer> getDistancesHilltopAndEndOfHill() {
        return distancesHilltopAndEndOfHill;
    }

    public double getAvgDistanceHilltopAndEndOfHill() {
        return avgDistanceHilltopAndEndOfHill;
    }

    public ArrayList<Double> getTilts() {
        return tilts;
    }

    public double getAvgTilt() {
        return avgTilt;
    }

    public double getRelativeHilltopHeight() {
        return relativeHilltopHeight;
    }
}

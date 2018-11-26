class Hill {
    private boolean isA;
    private final String colorA = "16776960";
    private final String colorB = "16711680";
    private String colorTop;
    private final String colorEndOfSlope = "0";
    private final String colorSlope = "16777215";
    private double hilltopHigh;
    private final int maxX = 3000;
    private final int maxY = 4943;
    private final double minAvgHighDifference = 1.1;
    private final double maxDifferenceToHilltop = 2.0;
    private final double avgSlopeDifferenceInit = 5.0;
    private String[][] data;

    Hill(String[][] data, int x, int y, boolean isA) {
        this.isA = isA;
        this.data = data;
        this.hilltopHigh = Double.parseDouble(data[x][y]);
        markHill(x, y);
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
        //right
        markHill(x, y, goRight, stay, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //left
        markHill(x, y, goLeft,  stay, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //up
        markHill(x, y, stay,    goUp, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //down
        markHill(x, y, stay,    goDown, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //up left
        markHill(x, y, goLeft,  goUp, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //up right
        markHill(x, y, goRight, goUp, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //down left
        markHill(x, y, goLeft,  goDown, this.avgSlopeDifferenceInit, this.hilltopHigh);
        //down right
        markHill(x, y, goRight, goDown, this.avgSlopeDifferenceInit, this.hilltopHigh);

    }

    private boolean markHill(int x, int y, int xOffset, int yOffset, double averageSlope, double oldHilltop) {
        //If the new Position in still in the data array do...
        y = y + yOffset;
        x = x + xOffset;
        if(y < this.maxY && y > 0 && x < this.maxX && x > 0) {
            double newHilltop = Double.parseDouble(data[x][y]);
            //Recognise if its still the part of the hilltop and mark it as such
            if(newHilltop < this.hilltopHigh + this.maxDifferenceToHilltop && newHilltop > this.hilltopHigh - this.maxDifferenceToHilltop){
                data[x][y] = this.colorTop;
                return markHill(x, y, xOffset, yOffset, averageSlope, newHilltop);
            }else {
                //Recognise if the new position is part of the slope
                if (newHilltop < oldHilltop) {
                    averageSlope = (averageSlope + (oldHilltop - newHilltop)) / 2;
                    //Recognize if its still steep enough
                    if (averageSlope < this.minAvgHighDifference) {
                        this.data[x][y] = this.colorEndOfSlope;
                        return markHill(x, y, xOffset, yOffset, averageSlope, newHilltop);
                    } else {
                        this.data[x][y] = this.colorSlope;
                        return markHill(x, y, xOffset, yOffset, averageSlope, newHilltop);
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}

/**
 * @author: Man Vu
 * @student_number: 000801665
 * @created_date: 9/17/2020
 * This file is created by Man Vu and is not available to anyone
 */
public class ClosestPairs {
    private float closestDistance;
    private PeakPair[] closestPairsList;
    private int counter;

    /**
     * Constructor for ClosestPairs
     */
    public ClosestPairs() {
        closestDistance = Float.MAX_VALUE;
        counter = 0;
        closestPairsList = new PeakPair[100];
    }

    /**
     * This method will insert a new peakPair into closestPeakPairsList if it has not existed in the array
     * @param peakPair
     */
    public void insertClosestPair(PeakPair peakPair) {

        for (int i = 0; i < counter; i++) {
            if (peakPair.getA().getRow() == closestPairsList[i].getA().getRow() || peakPair.getB().getCol() == closestPairsList[i].getB().getCol()) {
                return;
            }
        }
        closestPairsList[counter] = peakPair;
        counter++;
    }

    /**
     * Getter for closestDistance
     * @return
     */
    public float getClosestDistance() {
        return closestDistance;
    }

    /**
     * This method will set a new closest distance only if passed-in distance parameter is smaller than closestDistance
     * @param distance
     */
    public void setNewClosestDistance(float distance) {
        if (distance < this.closestDistance) {
            this.closestDistance = distance;
            closestPairsList = new PeakPair[100];
            counter = 0;
        }
    }

    /**
     * Getter for counter
     * @return
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Setter for counter
     * @param counter
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }

    /**
     * This method will print string that will be used for reporting purposes
     * @return
     */
    @Override
    public String toString() {
        String string = "";

        for (int i = 0; i < counter; i++) {
            string += String.format("The distance between two peaks = %.2f m peaks are [%d,%d elevation = %d] and [%d,%d elevation = %d]\n",
                    closestDistance,
                    closestPairsList[i].getA().getCol(), closestPairsList[i].getA().getRow(), closestPairsList[i].getA().getElevation(),
                    closestPairsList[i].getB().getCol(), closestPairsList[i].getB().getRow(), closestPairsList[i].getB().getElevation());
        }

        return string;
    }
}

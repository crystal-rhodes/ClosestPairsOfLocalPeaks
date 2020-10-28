/**
 * @author: Man Vu
 * @student_number: 000801665
 * @created_date: 9/17/2020
 * This file is created by Man Vu and is not available to anyone
 */
public class PeakPair {
    private Peak A;
    private Peak B;

    /**
     * Constructor for PeakPair
     */
    public PeakPair() {
    }

    /**
     * Constructor for PeakPair
     * @param a
     * @param b
     */
    public PeakPair(Peak a, Peak b) {
        setA(a);
        setB(b);
    }

    /**
     * Get the distance between any two Peaks
     * @param peak
     * @param peak1
     * @return
     */
    public static float getDistance(Peak peak, Peak peak1) {
        return (float) Math.sqrt(Math.pow(peak.getRow() - peak1.getRow(), 2) + Math.pow(peak.getCol() - peak1.getCol(), 2));
    }

    /**
     * Getter for Peak A
     * @return
     */
    public Peak getA() {
        return A;
    }

    /**
     * Setter for Peak A
     * @param a
     */
    public void setA(Peak a) {
        A = a;
    }

    /**
     * Getter for Peak B
     * @return
     */
    public Peak getB() {
        return B;
    }

    /**
     * Setter for Peak B
     * @param b
     */
    public void setB(Peak b) {
        B = b;
    }

    /**
     * Get the distance between two Peaks
     * @return
     */
    public double getDistance() {
        return Math.sqrt(Math.pow(A.getRow() - B.getRow(), 2) + Math.pow(A.getCol() - B.getCol(), 2));
    }
}

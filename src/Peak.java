/**
 * @author: Man Vu
 * @student_number: 000801665
 * @created_date: 9/17/2020
 * This file is created by Man Vu and is not available to anyone
 */
public class Peak implements Comparable<Peak> {
    private int row;
    private int col;
    private int elevation;

    /**
     * Constructor for Peak
     * @param row
     * @param col
     * @param elevation
     */
    public Peak(int row, int col, int elevation) {
        this.row = row;
        this.col = col;
        this.elevation = elevation;
    }

    /**
     * Getter for Row
     * @return
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter for Column
     * @return
     */
    public int getCol() {
        return col;
    }

    /**
     * Comparator for sorting by Y-Coordinates
     * @param o
     * @return
     */
    @Override
    public int compareTo(Peak o) {
        int a = 0;
        return Integer.compare(this.col, o.getCol());
    }

    /**
     * Get the elevation
     * @return
     */
    public int getElevation() {
        return elevation;
    }
}

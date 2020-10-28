/**
 * @author: Man Vu
 * @student_number: 000801665
 * @created_date: 9/17/2020
 * <p>
 * This file is created by Man Vu and is not available to anyone
 */

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Assignment1 {
    public static final int PEAK_NOMINATED_VALUE = 98480;
    public static final String FILENAME = "src/ELEVATIONS.TXT";
    public static final int MAXIMUM_POSSIBLE_ELEVATION = 99000;
    public static final int MINIMUM_POSSIBLE_ELEVATION = 15000;

//    public static final int PEAK_NOMINATED_VALUE = 90;
//    public static final String FILENAME = "src/SAMPLE.TXT";
//    public static final int MAXIMUM_POSSIBLE_ELEVATION = 99;
//    public static final int MINIMUM_POSSIBLE_ELEVATION = 1;

    public static void main(String[] args) {
        int numberOfRows, numberOfColumns, exclusionRadius = 0;
        int[][] matrix = {};
        // Initialize an array with the size of the highest elevation + 1
        // frequency is used for requirements #1 and #4
        int[] frequency = new int[MAXIMUM_POSSIBLE_ELEVATION + 1];
        int row = 0;
        int col = 0;

        File file = new File(FILENAME);

        try {
            Scanner inputFile = new Scanner(file);

            // Get the numbers of rows, columns, and the exclusion radius from the file
            numberOfRows = inputFile.nextInt();
            numberOfColumns = inputFile.nextInt();
            exclusionRadius = inputFile.nextInt();

            // Allocate memory for the 2D array with the specified numbers of rows and columns
            matrix = new int[numberOfRows][numberOfColumns];

            // Enter each elevation from the file
            while (inputFile.hasNext() && row < numberOfRows) {
                matrix[row][col] = inputFile.nextInt();
                col++;

                if (col == numberOfColumns) {
                    row++;
                    col = 0;
                }
            }

            // Close the file after reading input
            inputFile.close();

        } catch (IOException ex) {
            System.out.println("Error Reading File " + ex.getMessage());
        }

        // START TIMED EXECUTION
        long startTime = System.nanoTime();

        int[] mostFrequent = mostFrequent(matrix, frequency); // requirement #4
        Peak[] localPeaks = findLocalPeaksCompleteSearch(matrix, exclusionRadius); // requirement #2
        ClosestPairs obj = findClosestPairsOptimized(localPeaks); // requirement #3
        int[] lowest = lowestElevation(frequency); // requirement #1

        long stopTime1 = System.nanoTime();
        // END TIMED EXECUTION

        System.out.print("\nExecution completed ");
        System.out.printf(" [Time = %d us/ %d ms / %d s]\n",
                (stopTime1 - startTime) / 1000,
                (stopTime1 - startTime) / 1000000,
                (stopTime1 - startTime) / 1000000000);


        System.out.println("Lowest elevation is " + lowest[0] + " and its frequency is " + lowest[1]);
        System.out.println("The number of local peaks is " + localPeaks.length);
        System.out.printf("The most common elevation: %d, frequency count: %d", mostFrequent[0], mostFrequent[1]);
        System.out.println("The number of occurrences of the closest distance is " + obj.getCounter());
        System.out.println(obj.toString());

    }

    /**
     * This method runs in O(n(logn)^2) to find closest pairs and this can be further improved to O(nlogn)
     *
     * @param localPeaks
     * @return
     */
    private static ClosestPairs findClosestPairsOptimized(Peak[] localPeaks) {
        // Initialize ClosestPairs object to store information about closest pairs in the matrix
        ClosestPairs closestPairs = new ClosestPairs();
        findingClosestHelper(localPeaks, localPeaks.length, closestPairs);

        return closestPairs;
    }

    /**
     * This solution uses Divide and Conquer algorithmic paradigm to find the closest pairs
     *
     * @param localPeaks
     * @param n
     * @param closestPairs
     * @return
     */
    private static float findingClosestHelper(Peak[] localPeaks, int n, ClosestPairs closestPairs) {
        // If there remains fewer than 3 localPeaks, perform bruteForce because it is faster
        if (n <= 3) {
            return bruteForce(localPeaks, n, closestPairs);
        }

        // Find the middle point
        int mid = n / 2;
        Peak middlePoint = localPeaks[mid];

        // Imagine there is a vertical line passing through the middle of the matrix,
        // then calculate the smallest distance distanceLeft on left of middle point and distanceRight on the right side
        float distanceLeft = findingClosestHelper(Arrays.copyOf(localPeaks, mid), mid, closestPairs);
        float distanceRight = findingClosestHelper(Arrays.copyOfRange(localPeaks, mid, localPeaks.length), n - mid, closestPairs);

        // Decide on whether distanceLeft to the left of the middle point or distanceRight to the left of the middle point is the minimum distance
        float nearerDistanceToMidpoint = Math.min(distanceLeft, distanceRight);

        // Initialize an array of localPeaks so that closestPairs will be filtered out later
        Peak[] strip = new Peak[localPeaks.length];
        int stripCount = 0;

        // Iterate localPeaks array and insert Peaks that have shorter horizontal distance (based on X-coordinates) to the middle point
        for (int i = 0; i < n; i++) {
            if (Math.abs(localPeaks[i].getRow() - middlePoint.getRow()) < nearerDistanceToMidpoint) {
                strip[stripCount] = localPeaks[i];
                stripCount++;
            }
        }

        // Send this strip array to stripClosest so that it will filter the closest pairs
        return stripClosest(Arrays.copyOf(strip, stripCount), stripCount, nearerDistanceToMidpoint, closestPairs);
    }

    /**
     * This method finds the closest pairs among those in the localPeaks passed in
     *
     * @param localPeaks   is always <= 3
     * @param n
     * @param closestPairs
     * @return
     */
    private static float bruteForce(Peak[] localPeaks, int n, ClosestPairs closestPairs) {
        float minDistance = Float.MAX_VALUE;

        // Iterate over the localPeaks and find the closest pairs
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                float distance = PeakPair.getDistance(localPeaks[i], localPeaks[j]);
                if (distance < minDistance) {
                    minDistance = distance;
                }

                if (distance <= closestPairs.getClosestDistance()) {
                    closestPairs.setNewClosestDistance(distance);
                    closestPairs.insertClosestPair(new PeakPair(localPeaks[i], localPeaks[j]));
                }
            }
        }

        return minDistance;
    }

    /**
     * This method will strip the closest distances and put them in ClosestPairs object for future reference
     *
     * @param strip
     * @param length
     * @param minDistance
     * @param closestPairs
     * @return
     */
    private static float stripClosest(Peak[] strip, int length, float minDistance, ClosestPairs closestPairs) {
        // Sort the Peaks in strip array by Y-coordinates
        Arrays.sort(strip);

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length && (strip[j].getCol() - strip[i].getCol() < minDistance); j++) {
                // Get the distance of every two peaks
                float distance = PeakPair.getDistance(strip[i], strip[j]);

                // Update the minDistance if the current distance between two peaks in strip is smaller than it
                if (distance < minDistance) {
                    minDistance = distance;
                }

                // Update the object closestPairs by inserting new PeakPair if distance is smaller than the current smallest distance of the object
                if (distance <= closestPairs.getClosestDistance()) {
                    closestPairs.setNewClosestDistance(distance);
                    closestPairs.insertClosestPair(new PeakPair(strip[i], strip[j]));
                }
            }
        }


        return minDistance;
    }

    /**
     * @param matrix          - matrix entered from file
     * @param exclusionRadius - exclusion radius from the file
     * @return an array of Local Peaks found
     */
    public static Peak[] findLocalPeaksCompleteSearch(int[][] matrix, int exclusionRadius) {
        // Calculate the maximum number of local peaks
        int maximumPossibleNumberOfLocalPeaks = (int) ((matrix.length * matrix[0].length) / Math.pow(exclusionRadius, 2));

        // Initialize a partially-filled array of Local Peaks
        Peak[] localPeaks = new Peak[maximumPossibleNumberOfLocalPeaks];
        int count = 0;

        // Iterate over each elevation in the matrix and check if it is a local peak
        for (int row = exclusionRadius; row < matrix.length - exclusionRadius; row++) {
            for (int col = exclusionRadius; col < matrix[row].length - exclusionRadius; col++) {
                if (matrix[row][col] >= PEAK_NOMINATED_VALUE) {
                    boolean stop = false;

                    // Examine every single elevation in the matrix and terminate immediately if find a local peak within the search area
                    for (int localRow = row - exclusionRadius; localRow < row + exclusionRadius + 1 && !stop; localRow++) {
                        for (int localCol = col - exclusionRadius; localCol < col + exclusionRadius + 1 && !stop; localCol++) {
                            if (localRow == row && localCol == col) {
                                continue;
                            }
                            if (matrix[localRow][localCol] >= matrix[row][col]) {
                                stop = true;
                            }
                        }
                    }

                    if (!stop) {
                        localPeaks[count] = new Peak(row, col, matrix[row][col]);
                        count++;

                        // Shift the searching target by exclusion radius + 1 to the right if found a local peak
                        col += exclusionRadius + 1;
                    }
                }
            }
        }

        // Return a new trimmed array
        return Arrays.copyOf(localPeaks, count);
    }

    /**
     * Not used. Brute-force solution for #3. Time Complexity O(n^2) - Neither memory-efficient nor time-efficient
     * This approach would result in Memory Limit Exceeded if the size of the matrix is larger than 12000x1250 on 16 GB Ram computer
     *
     * @param localPeaks
     */
    public static void findClosestPairs(Peak[] localPeaks) {
        PeakPair[][] peakPairs = new PeakPair[localPeaks.length][localPeaks.length];

        for (int i = 0; i < localPeaks.length; i++) {
            for (int j = 0; j < localPeaks.length; j++) {
                peakPairs[i][j] = new PeakPair(localPeaks[i], localPeaks[j]);

            }
        }

        double smallestDistance = Float.MAX_VALUE;
        PeakPair smallestPeakPairObject = null;
        int numberOfOccurences = 0;
        for (int i = 0; i < localPeaks.length; i++) {
            for (int j = 0; j < localPeaks.length; j++) {
                if (smallestDistance == peakPairs[i][j].getDistance()) {
                    numberOfOccurences++;
                }
                if (smallestDistance > peakPairs[i][j].getDistance()) {
                    smallestDistance = peakPairs[i][j].getDistance();
                    smallestPeakPairObject = peakPairs[i][j];
                    numberOfOccurences = 0;
                }
            }
        }

        System.out.println(smallestPeakPairObject.toString() + " | Number of Occurences: " + numberOfOccurences);
    }

    /**
     * This method will find the lowest elevation in the matrix and the idea is to find the first element in the frequency array
     * that has frequency higher than 0 when iterating from left to right
     *
     * @param frequencyArray
     * @return
     */
    public static int[] lowestElevation(int[] frequencyArray) {
        int lowestElevationFrequency = 0, lowestElevation = 0;
        for (int key = MINIMUM_POSSIBLE_ELEVATION; key < frequencyArray.length; key++) {
            if (frequencyArray[key] != 0) {
                lowestElevationFrequency = frequencyArray[key];
                lowestElevation = key;
                break;
            }
        }
        return new int[]{lowestElevation, lowestElevationFrequency};
    }

    /**
     * This method will implement simple hashing on array which means it will make use of the indices of the frequency array
     * and use them as elevation values. The elements of these indices are frequency values.
     *
     * @param matrix
     * @return
     */
    public static int[] mostFrequent(int[][] matrix, int[] frequencyArray) {
        int mostFrequency = 0;
        int mostFrequentElevation = -1;

        // By default, all elements in the frequencyArray are 0 after initialization
        // Iterate over the matrix and increment the frequency of every elevation by 1
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                int elevation = matrix[row][col];
                frequencyArray[elevation]++;

                // Update most frequency elevation and frequency if found a new elevation has the higher frequency
                if (frequencyArray[elevation] != 0 && frequencyArray[elevation] > mostFrequency) {
                    mostFrequency = frequencyArray[elevation];
                    mostFrequentElevation = elevation;
                }
            }
        }

        return new int[]{mostFrequentElevation, mostFrequency};
    }
}

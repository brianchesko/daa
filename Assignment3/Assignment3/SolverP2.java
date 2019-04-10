import java.util.Arrays;

/**
 * Brian Chesko
 * DAA Programming Assignment 3 Pt 2
 * Fully complete and tested, 2019/03/20
 */
public class SolverP2 extends Solver {
    private short[][] matrix;
    private short[][] swapMatrix;
    private int size;
    private int highestProductivity;
    private long solutionsExplored;
    private long partialExplored;
    private int[] bestArrangement;

    public SolverP2(short[][] jobEmployeeMatrix) {
        this.matrix = jobEmployeeMatrix;
        this.size = matrix.length;
        this.highestProductivity = 0;
        this.solutionsExplored = 0;
        this.partialExplored = 0;
        this.bestArrangement = null;
        this.createAndSwapMatrix();
    }

    /**
     * Reinitialize the solver to work with the specified matrix.
     * @param jobEmployeeMatrix The new matrix to solve.
     */
    public void setJobEmployeeMatrix(short[][] jobEmployeeMatrix) {
        this.matrix = jobEmployeeMatrix;
        this.size = matrix.length;
        this.highestProductivity = 0;
        this.solutionsExplored = 0;
        this.partialExplored = 0;
        this.bestArrangement = null;
        this.createAndSwapMatrix();
    }

    /**
     * Debug method for printing matrices
     */
    public void printMatrices() {
        System.out.println("== Sorted job/employee matrix ==");
        for (short[] row : matrix) {
            for (short val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
        System.out.println("== Swap matrix ==");
        for (short[] row : swapMatrix) {
            for (short val : row) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }

    /**
     * Sorts each row of the matrix in descending productivity order
     * via insertion sort. Insertion sort is used because it's faster
     * for small n (in fact the standard JDK Arrays.sort method uses
     * insertion sort for n<47) as well as being simple to implement.
     *
     * The swap matrix created stores the original column in the new
     * column, such that swapMatrix[i][j] is the original column that
     * matrix[i][j] corresponded to.
     */
    private void createAndSwapMatrix() {
        swapMatrix = new short[size][size];
        for (int i = 0; i < size; i++) {
            swapMatrix[i][0] = 0; // Initial value
            // Sort row i by insertion sort
            for (short j = 1; j < size; j++) {
                swapMatrix[i][j] = j; // Initial value
                // Store working value in a short just so we don't
                // have to cast ever.
                short currValue = matrix[i][j];
                // Find ending position for current index j
                int endPos = 0;
                while (endPos < j && matrix[i][endPos] >= currValue) {
                    endPos++;
                }
                // Shift values at [endPos, j - 1] right by 1, working
                // right to left
                for (int k = j - 1; k >= endPos; k--) {
                    matrix[i][k + 1] = matrix[i][k];
                    swapMatrix[i][k + 1] = swapMatrix[i][k];
                }
                // Put working value at its destination
                matrix[i][endPos] = currValue;
                swapMatrix[i][endPos] = j;
            }
        }
    }

    /**
     * Finds the most productive set of jobs assignments given the job
     * employee matrix.
     * @return an array containing the most productive arrangement, such that
     * array[i] is the best job for employee i.
     */
    public int[] solve() {
        // Already solved for this matrix, return previous solution.
        if (bestArrangement != null)
            return bestArrangement;

        bestArrangement = new int[size];
        int[] arrangement = new int[size];
        int[] partialProductivities = new int[size];
        int[] maxSubtreeProductivities = new int[size];
        boolean[] columnUsed = new boolean[size];
        // Ensure prefill arrangement to all unused
        for (int i = 0; i < size; i++) {
            arrangement[i] = -1;
            // Do preprocessing to determine largest productivity of each
            // size subtree.  This lets us prune subtree searches that cannot
            // possibly beat a current max partial assignment.
            int largestVal = Integer.MIN_VALUE;
            for (int val : matrix[i]) {
                if (val > largestVal) {
                    largestVal = val;
                }
            }
            for (int j = 0; j <= i; j++) {
                maxSubtreeProductivities[j] += largestVal;
            }

        }

        int emp = 0;
        int job;

        while (emp < size && emp >= 0) {
            int prevJob = arrangement[emp];
            if (prevJob != -1) {
                // Don't check the same index twice for the same employee,
                // move to the next job.
                job = prevJob + 1;
                // Reset variables tracking the previous setup.
                columnUsed[swapMatrix[emp][prevJob]] = false;
                arrangement[emp] = -1;
            } else {
                // Haven't seen this employee before (for this subtree)
                // so start from beginning of job search.
                job = 0;
            }
            boolean foundCol = false;

            // If the partial solution + the largest combination after this is less
            // than the highest seen, we can't possibly beat it. Skip that subtree.
            int prodSoFar = emp == 0 ? 0 : partialProductivities[emp - 1];
            if (prodSoFar + maxSubtreeProductivities[emp] > highestProductivity) {
                while (job < size && !foundCol) {
                    if (!columnUsed[swapMatrix[emp][job]]) {
                        foundCol = true;
                        columnUsed[swapMatrix[emp][job]] = true;
                        arrangement[emp] = job;
                        partialProductivities[emp] = prodSoFar + matrix[emp][job];
                    } else {
                        job++;
                    }
                }
            } else {
                this.partialExplored++;
            }

            // We ALWAYS backtrack after the emp == size - 1 iteration.
            // We also ALWAYS backtrack after job == size - 1, but ONLY
            // after visiting children trees (if necessary).
            if (emp == size - 1) {
                this.solutionsExplored++;
                int partialProductivity = partialProductivities[emp];
                if (foundCol) {
                    // Save new best solution, if needed
                    if (partialProductivity > highestProductivity) {
                        highestProductivity = partialProductivity;
                        for (int i = 0; i < size; i++) {
                            bestArrangement[i] = swapMatrix[i][arrangement[i]];
                        }
                    }
                    // Reset tracking variables for this setup so we can search more
                    columnUsed[swapMatrix[emp][arrangement[emp]]] = false;
                    arrangement[emp] = -1;
                }

                // Reset tracking variables for this position
                emp--;
            } else if (!foundCol) {
                // Not the last employee, but still need to backtrack.
                emp--;
            } else {
                emp++;
            }
        }

        return bestArrangement;
    }

    /**
     * @return the best job assignment for the current matrix.
     */
    public int[] getBestArrangement() {
        return bestArrangement;
    }

    /**
     * @return the number of partial  solutions explored while finding the
     * best assignment
     */
    public long getPartialExploredSize() {
        return partialExplored;
    }

    /**
     * @return the number of full successful  solutions explored while
     * finding the best assignment
     */
    public long getFullyExploredSize() {
        return solutionsExplored;
    }

    /**
     * @return the total number of solutions explored while finding the best
     * assignment
     */
    public long getTotalExploredSize() {
        return partialExplored + solutionsExplored;
    }

    /**
     * @return the overall productivity of the best assignment
     */
    public int getSolutionProductivity() {
        return highestProductivity;
    }

}

import java.util.*;
import java.util.function.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Brian Chesko
 * DAA Programming Assignment 2
 * Fully complete and tested, 2019/02/27
 * Modified 2019/03/20
 */
public class Solver {
    private short[][] matrix;
    private int size;
    private int highestProductivity;
    private long solutionsExplored;
    private long partialExplored;
    private int[] bestArrangement;

    public Solver(short[][] jobEmployeeMatrix) {
        this.matrix = jobEmployeeMatrix;
        this.size = matrix.length;
        this.highestProductivity = 0;
        this.solutionsExplored = 0;
        this.partialExplored = 0;
        this.bestArrangement = null;
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

        //int positionsPerThread = 2;
        int numThreads = 4;//(size + positionsPerThread - 1) / positionsPerThread;
        int positionsPerThread = (size + numThreads - 1) / numThreads;
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        // Create one thread for every set of 'positionsPerThread' starting positions
        for (int threadNo = 0; threadNo < numThreads; threadNo++) {
            int start = threadNo * positionsPerThread;
            SolvingPartition partition = new SolvingPartition(
                    start,
                    threadNo == numThreads - 1 ? size : start + positionsPerThread,
                    this
            );
            // Add the thread to the thread pool
            threadPool.execute(partition);
        }

        // Now all threads have been added to the thread pool,
        // try to close the process and wait until complete
        try {
            threadPool.shutdown();
            while (!threadPool.isTerminated()) {
                threadPool.awaitTermination(60, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            // Should do something more useful with this but it's fine
            e.printStackTrace();
        }

        return bestArrangement;
    }

    public void setHighestProductivity(int prod) {
        this.highestProductivity = prod;
    }

    public void setBestArrangement(int[] arr) {
        this.bestArrangement = arr.clone();
    }

    private class SolvingPartition implements Runnable {
        private int firstEmpStartJob;
        private int firstEmpEndJob;
        private Consumer<Solver> endCallback;
        private Solver wrapper;
        private int[] bestArrangement;
        private long solutions;
        private long partialSolutions;
        private int highestProductivity;

        /**
         * @param firstEmpStartJob the index of the first employee tree to check
         * @param firstEmpEndJob non inclusive index of last employee tree
         */
        SolvingPartition(int firstEmpStartJob, int firstEmpEndJob,
                Solver wrapper) {
            this.firstEmpStartJob = firstEmpStartJob;
            this.firstEmpEndJob = firstEmpEndJob;
            this.endCallback = (x) -> {
                if (x.getSolutionProductivity() < this.highestProductivity) {
                    x.setHighestProductivity(highestProductivity);
                    x.setBestArrangement(bestArrangement);
                }
                x.incrementFullExplored(solutions);
                x.incrementPartialExplored(partialSolutions);
            };
            this.solutions = 0;
            this.partialSolutions = 0;
            this.bestArrangement = new int[size];
            this.wrapper = wrapper;
            this.highestProductivity = 0;
        }

        @Override
        public void run() {
            int[] arrangement = new int[size];
            int[] partialProductivities = new int[size];
            boolean[] columnUsed = new boolean[size];
            short[][] matrix = deepMatrixCopy();
            // Ensure prefill arrangement to all unused
            for (int i = 0; i < size; i++) {
                arrangement[i] = -1;
            }

            int emp = 0;
            int job = firstEmpStartJob;

            while (emp < size && emp >= 0) {
                int lastJobToCheck = emp == 0 ? firstEmpEndJob : size;
                int prevJob = arrangement[emp];
                if (prevJob != -1) {
                    // Don't check the same index twice for the same employee,
                    // move to the next job.
                    job = prevJob + 1;
                    // Reset variables tracking the previous setup.
                    columnUsed[prevJob] = false;
                    arrangement[emp] = -1;
                } else if (emp > 0) {
                    // Haven't seen this employee before (for this subtree)
                    // so start from beginning of job search.
                    job = 0;
                }
                boolean foundCol = false;
                while (job < lastJobToCheck && !foundCol) {
                    partialSolutions++;
                    if (!columnUsed[job]) {
                        foundCol = true;
                        columnUsed[job] = true;
                        arrangement[emp] = job;
                        if (emp == 0) {
                            partialProductivities[emp] = matrix[emp][job]; //matrix.getProductivity(emp, job);
                        } else {
                            partialProductivities[emp] = partialProductivities[emp - 1] +  matrix[emp][job];//matrix.getProductivity(emp, job);
                        }
                    } else {
                        job++;
                    }
                }

                // We ALWAYS backtrack after the emp == size - 1 iteration.
                // We also ALWAYS backtrack after job == size - 1, but ONLY after visiting children trees (if necessary).
                if (emp == size - 1) {
                    int partialProductivity = partialProductivities[emp];
                    solutions++;
                    if (partialProductivity > highestProductivity) {
                        highestProductivity = partialProductivity;
                        bestArrangement = arrangement.clone();
                    }
                    // Reset tracking variables for this position
                    columnUsed[arrangement[emp]] = false;
                    arrangement[emp] = -1;
                    emp--;
                } else if (!foundCol) {
                    // Not the last employee, but still need to backtrack.
                    emp--;
                } else {
                    emp++;
                }
            }

            this.endCallback.accept(wrapper);
        }
    }

    /**
     * Converts the list into an int[] array for returning to the main program
     * @param arrayList
     * @return
     */
    private int[] convertToIntArray(List<Integer> arrayList) {
        int[] array = new int[arrayList.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = arrayList.get(i);
        }
        return array;
    }

    /**
     * @return the best job assignment for the current matrix.
     */
    public int[] getBestArrangement() {
        return bestArrangement;
    }

    /**
     * @return the total number of solutions explored while finding the best assignment
     */
    public long getExploredSize() {
        return solutionsExplored;
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
 
    public void incrementFullExplored(long sol) {
        this.solutionsExplored += sol;
    }

    public void incrementPartialExplored(long partials) {
        this.partialExplored += partials;
    }

    /**
     * @return the overall productivity of the best assignment
     */
    public int getSolutionProductivity() {
        return highestProductivity;
    }

    private short[][] deepMatrixCopy() {
        short[][] copy = new short[matrix.length][matrix[0].length];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = matrix[i][j];
            }
        }
        return copy;
    }

}

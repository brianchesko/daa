import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Brian Chesko
 * DAA Programming Assignment 3
 * Fully complete and tested, 2019/03/13
 * Modified 2019/03/31
 */
public class Driver {

    public static void main(String[] args) throws IOException {
        System.out.println("Enter matrix size");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(reader.readLine().trim());
        System.out.println(n);
        short[][] matrix = new short[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = (short) Integer.parseInt(reader.readLine().trim());
                System.out.printf("%d\t", matrix[i][j]);
            }
            System.out.println();
        }

        // Note: here, I had to resolve the weirdest bug. I was getting
        // different results after merging the solvers into one folder
        // despite not changing any meaningful code.
        // As it turned out, the matrices passed were being modified
        // across solvers which messed up 2/3 of the solvers final
        // results since they shared the same reference.
        Solver sol0 = new SolverP0(matrix);
        Solver sol1 = new SolverP1(deepMatrixCopy(matrix));
        Solver sol2 = new SolverP2(deepMatrixCopy(matrix));

        int[] solution = sol0.solve();
        sol1.solve();
        sol2.solve();

        System.out.println("== Number of partial assignments explored ==");
        System.out.printf("\tP0: %d \tP1: %d \tP2: %d\n", 
            sol0.getPartialExploredSize(),
            sol1.getPartialExploredSize(),
            sol2.getPartialExploredSize());
        System.out.println("==== Number of full assignments explored ===");
        System.out.printf("\tP0: %d \tP1: %d \tP2: %d\n", 
            sol0.getFullyExploredSize(),
            sol1.getFullyExploredSize(),
            sol2.getFullyExploredSize());
        System.out.println("=== Total number of assignments explored ===");
        System.out.printf("\tP0: %d \tP1: %d \tP2: %d\n", 
            sol0.getTotalExploredSize(),
            sol1.getTotalExploredSize(),
            sol2.getTotalExploredSize());
        System.out.println("Best job assignment is:");
        for (int i = 0; i < n; i++) {
            System.out.printf("Person %d assigned job %d\n", i, solution[i]);
        }
        System.out.println("Best job assignment cost");
        System.out.printf("\tP0: %d \tP1: %d \tP2: %d\n", 
            sol0.getSolutionProductivity(),
            sol1.getSolutionProductivity(),
            sol2.getSolutionProductivity());
    }

    private static short[][] deepMatrixCopy(short[][] matrix) {
        short[][] copy = new short[matrix.length][matrix[0].length];
        for (int i = 0; i < copy.length; i++) {
            for (int j = 0; j < copy[0].length; j++) {
                copy[i][j] = matrix[i][j];
            }
        }
        return copy;
    }

}

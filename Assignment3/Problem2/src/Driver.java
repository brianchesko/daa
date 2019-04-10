import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Brian Chesko
 * DAA Programming Assignment 3 Pt 2
 * Fully complete and tested, 2019/03/20
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

        Solver solver = new Solver(matrix);

        int[] sol = solver.solve();
        System.out.printf("Number of partial assignments explored: %d\n",
            solver.getPartialExploredSize());
        System.out.printf("Number of full assignments explored: %d\n",
            solver.getFullyExploredSize());
        System.out.printf("Total number of assignments explored: %d\n",
            solver.getTotalExploredSize());
        System.out.println("Best job assignment is:");
        for (int i = 0; i < sol.length; i++) {
            System.out.printf("Person %d assigned job %d\n", i, sol[i]);
        }
        System.out.printf("Best job assignment cost: %d\n", solver.getSolutionProductivity());
    }
}

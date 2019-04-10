import java.math.BigInteger;
import java.util.function.IntBinaryOperator;

public class Binomial {
    public static void main(String[] args) {
        testRecursiveBin();    
    }

    public static void testRecursiveBin() {
        int[] nSet = {0, 100, 100, 100, 7, 6, 6, 6, 4};
        int[] kSet = {0, 0,   1,   99,  7, 4, 5, 3, 2};

        applyTestSet("Recursive", Binomial::recursiveBin, 
                nSet, kSet);
        applyTestSet("Iterative", Binomial::iterativeBinomialCoeff, 
                nSet, kSet);
        applyTestSet("Formula-based", Binomial::formulaBinomialCoeff, 
                nSet, kSet);

        for (int n = 1; n < 9; n++) {
            System.out.println("-- Pascal's triangle using "
                    + "iterative (n = " + n + ") --");
            drawPascalIterative(n);
        }
    }

    private static void applyTestSet(String methodName, IntBinaryOperator f,
                int[] nSet, int[] kSet) {
        System.out.println(" Test suite for " + methodName + " coeffs");
        for (int i = 0; i < nSet.length; i++) {
            int n = nSet[i];
            int k = kSet[i];
            System.out.printf("%s binomial C(%d, %d): %d\n", methodName, n, k,
                    f.applyAsInt(n, k));
        }
    }

    public static int recursiveBin(int n, int k) {
        if (k == 0 || n == k) {
            return 1;
        } else {
            return recursiveBin(n - 1, k) + recursiveBin(n - 1, k - 1);
        }
    }

    public static void drawPascalIterative(int n) {
        int[][] pascal = new int[n + 1][n + 1];
        int lastLine = n;

        for (n = 0; n <= lastLine; n++) {
            pascal[n][0] = 1;
            System.out.print(1 + " ");
            for (int k = 1; k < n; k++) {
                pascal[n][k] = pascal[n - 1][k] + pascal[n - 1][k-1];
                System.out.print(pascal[n][k] + " ");    
            }
            pascal[n][n] = 1;
            System.out.print(n > 0 ? "1\n" : "\n");
        }
    }

    public static int iterativeBinomialCoeff(int n, int k) {
        int[][] pascal = new int[n + 1][k + 1];
        int lastLine = n;
        int lastCol = k;

        for (n = 0; n <= lastLine; n++) {
            pascal[n][0] = 1;
            //Only need to store the values in the diagonals above (n, k)
            //Start at (n, n - k) in each row, but minimum of (n, 1)
            //and maximum of (n, k-1). i.e. does not calculate any unused
            //coefficients anywhere in the matrix.
            if (lastCol > 0) {
                for (k = (n > lastCol && n <= 2 * lastCol ? n - lastCol : 1); k < n && k <= lastCol; k++) {
                    pascal[n][k] = pascal[n - 1][k] + pascal[n - 1][k-1];
                }

                if (n <= lastCol) {
                    pascal[n][n] = 1;
                }
            }
        }

        return pascal[lastLine][lastCol];
    }

    public static int formulaBinomialCoeff(int n, int k) {
        //Large overlap between top and bottom factorial
        if (n - k + 1 < k) {
            return fallingFactorial(n, k + 1) / fallingFactorial(n - k, 1);   
        } else {
            return fallingFactorial(n, n - k + 1) / fallingFactorial(k, 1);
        }
    }

    public static BigInteger bigIntBinomialCoeff(int n, int k) {
        //Large overlap between top and bottom factorial
        if (n - k + 1 < k) {
            return bigIntFallingFactorial(n, k + 1).divide(bigIntFallingFactorial(n - k, 1));
        } else {
            return bigIntFallingFactorial(n, n - k + 1).divide(bigIntFallingFactorial(k, 1));
        }
    }

    private static BigInteger bigIntFallingFactorial(int n, int min) {
        BigInteger prod = BigInteger.ONE;
        
        while (n >= min) {
            prod = prod.multiply(BigInteger.valueOf(n));
            n--;
        }

        return prod;
    }

    private static int fallingFactorial(int n, int min) {
        int prod = 1;
    
        while (n >= min) {
            prod *= n;
            n--;
        }

        return prod;
    }

    public static void drawPascalIterative1DArray(int n) {
        int lastLine = n;
        int[] pascal = new int[n / 2 + 1];

        pascal[0] = 1;

        for (n = 0; n <= lastLine; n++) {
            int temp1 = 1;
            int temp2 = 0;
            //True if n  is even - must use the last number of
            //above line twice.
            boolean evenLine = (n % 2 == 0); 
            int lineSize = n / 2 + 1;
            System.out.print("1 ");

            for (int k = 1; k < lineSize; k++) {
                int left = pascal[k - 1];
                int right = (evenLine && k == lineSize - 1) ? left : pascal[k];
                int next = left + right;
                if (k % 2 == 0) {
                    temp1 = next;
                    pascal[k - 1] = temp2;
                } else {
                    temp2 = next;
                    pascal[k - 1] = temp1;
                }
                System.out.print(next + " ");
            }


            pascal[lineSize - 1] = (lineSize % 2 == 0) ? temp2 : temp1;
            for (int k = lineSize - (evenLine ? 2 : 1); k >= 0; k--) {
                System.out.print(pascal[k] + " ");
            }

            System.out.println();            
        }
    }
}

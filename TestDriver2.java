

public class TestDriver2 {
    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            // for (int j = 0; j <= i; j++) {
            int j = i / 2;
                System.out.printf("(%d, %d) = %s%n", i, j, Binomial.bigIntBinomialCoeff(i, j).toString());
            // }
            System.out.println();
        }    
    }
}

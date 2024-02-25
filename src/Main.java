import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        CSVReader rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/train.csv")).getFile());
        rdr.nextLine();
        int id;
        double[] xData = new double[10000 * 10];
        double[] yData = new double[10000];
        for (int i = 0; i < 10000; ++i) {
            id = rdr.nextInt();
            yData[id] = rdr.nextDouble();
            for (int j = 0; j < 10; ++j)
                xData[id + j * 10000] = rdr.nextDouble();
        }
        long stop = System.currentTimeMillis();
        System.out.println("Scanning: " + (stop - start) + "ms");
        start = System.currentTimeMillis();
        Matrix X = new Matrix(10000, 10, xData);
        Matrix XT = X.transpose(new Matrix(10, 10000));
        Matrix XTXinv = XT.mul(X, new Matrix(10, 10)).invert();
        Matrix y = new Matrix(10000, 1, yData);
        Matrix w = new Matrix(10, 1);
        XTXinv.mul(XT.mul(y, w), w);
        stop = System.currentTimeMillis();
        System.out.println("Regression: " + (stop - start) + "ms");
        System.out.println(w);
        Matrix.printSystemInfo();
    }
}
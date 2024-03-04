import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        /*
        long start = System.currentTimeMillis(); //Debugging
        CSVReader rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/train.csv")).getFile()); //Read from train.csv
        rdr.nextLine(); //Consume the first line (column headers)
        int id; //The training sample id
        double[] XData = new double[10000 * 10]; //The data for the X matrix
        double[] yData = new double[10000]; //The data for the y vector
        for (int i = 0; i < 10000; ++i) { //Read all 10000 training samples
            id = rdr.nextInt(); //Get the id
            yData[id] = rdr.nextDouble(); //Store the average value into the y vector
            for (int j = 0; j < 10; ++j) //Read all 10 x_i values that are averaged over
                XData[id + j * 10000] = rdr.nextDouble(); //Store the x_i value into the matrix (remember, it's column-major)
        }
        rdr.close();
        long stop = System.currentTimeMillis(); //Debugging
        System.out.println("Scanning: " + (stop - start) + "ms"); //Debugging

        start = System.currentTimeMillis(); //Debugging
        Matrix X = new Matrix(10000, 10, XData); //Create the matrix X with the data xData
        Matrix XT = X.transpose(new Matrix(10, 10000)); //Create the matrix XT (X transposed)
        Matrix XTXinv = XT.mul(X, new Matrix(10, 10)).invert(); //Create the matrix XTX and invert it
        Matrix y = new Matrix(10000, 1, yData); //Create the vector y with the data yData
        Matrix w = new Matrix(10, 1); //Create the vector w
        XTXinv.mul(XT.mul(y, w), w); //Calculate the value of vector w via the formula from the lecture
        stop = System.currentTimeMillis(); //Debugging
        System.out.println("Regression: " + (stop - start) + "ms"); //Debugging
        System.out.println(w); //Print vector w
        Matrix.printSystemInfo(); //Debugging

        CSVWriter wtr = new CSVWriter(Objects.requireNonNull(Main.class.getResource("/")).getFile() + "solution.csv"); //Write to output.csv
        wtr.nextValues("Id", "y"); //Write the column names

        rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/test.csv")).getFile()); //Read from test.csv
        rdr.nextLine(); //Consume the first line (column headers)
        wtr.nextRecord(); //Advance to the next line
        double[] xData = new double[10]; //The data for the x vector
        Matrix x = new Matrix(10, 1, xData); //The x vector
        for (int i = 0; i < 2000; ++i) { //Read all 2000 test samples
            wtr.nextInt(rdr.nextInt()); //Write the id
            for (int j = 0; j < 10; ++j) //Read all 10 x_i values that are averaged over
                xData[j] = rdr.nextDouble(); //Store the x_i into the x vector
            wtr.nextDouble(w.dot(x)); //Write the computed average
            wtr.nextRecord(); //Advance to the next line
        }
        rdr.close(); //Close the reader

        wtr.close(); //Close the writer
        */
        /*
        long start = System.currentTimeMillis(); //Debugging
        CSVReader rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/train.csv")).getFile()); //Read from train.csv
        rdr.nextLine(); //Consume the first line (column headers)
        int id; //The training sample id
        BigDecimal[] XData = new BigDecimal[10000 * 10]; //The data for the X matrix
        BigDecimal[] yData = new BigDecimal[10000]; //The data for the y vector
        for (int i = 0; i < 10000; ++i) { //Read all 10000 training samples
            id = rdr.nextInt(); //Get the id
            yData[id] = rdr.nextBigDecimal(); //Store the average value into the y vector
            for (int j = 0; j < 10; ++j) //Read all 10 x_i values that are averaged over
                XData[id + j * 10000] = rdr.nextBigDecimal(); //Store the x_i value into the matrix (remember, it's column-major)
        }
        rdr.close();
        long stop = System.currentTimeMillis(); //Debugging
        System.out.println("Scanning: " + (stop - start) + "ms"); //Debugging

        start = System.currentTimeMillis(); //Debugging
        BigDecimalMatrix X = new BigDecimalMatrix(10000, 10, XData); //Create the matrix X with the data xData
        BigDecimalMatrix XT = X.transpose(new BigDecimalMatrix(10, 10000)); //Create the matrix XT (X transposed)
        BigDecimalMatrix XTXinv = XT.mul(X, new BigDecimalMatrix(10, 10)).invert(); //Create the matrix XTX and invert it
        BigDecimalMatrix y = new BigDecimalMatrix(10000, 1, yData); //Create the vector y with the data yData
        BigDecimalMatrix w = new BigDecimalMatrix(10, 1); //Create the vector w
        XTXinv.mul(XT.mul(y, w), w); //Calculate the value of vector w via the formula from the lecture
        stop = System.currentTimeMillis(); //Debugging
        System.out.println("Regression: " + (stop - start) + "ms"); //Debugging
        System.out.println(w); //Print vector w
        Matrix.printSystemInfo(); //Debugging

        CSVWriter wtr = new CSVWriter(Objects.requireNonNull(Main.class.getResource("/")).getFile() + "solution.csv"); //Write to output.csv
        wtr.nextValues("Id", "y"); //Write the column names

        rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/test.csv")).getFile()); //Read from test.csv
        rdr.nextLine(); //Consume the first line (column headers)
        wtr.nextRecord(); //Advance to the next line
        BigDecimal[] xData = new BigDecimal[10]; //The data for the x vector
        BigDecimalMatrix x = new BigDecimalMatrix(10, 1, xData); //The x vector
        for (int i = 0; i < 2000; ++i) { //Read all 2000 test samples
            id = rdr.nextInt();
            System.out.println(id);
            wtr.nextInt(id); //Write the id
            for (int j = 0; j < 10; ++j) //Read all 10 x_i values that are averaged over
                xData[j] = rdr.nextBigDecimal(); //Store the x_i into the x vector
            wtr.nextBigDecimal(w.dot(x)); //Write the computed average
            wtr.nextRecord(); //Advance to the next line
        }
        rdr.close(); //Close the reader

        wtr.close(); //Close the writer
         */
        CSVWriter wtr = new CSVWriter(Objects.requireNonNull(Main.class.getResource("/")).getFile() + "solution.csv"); //Write to output.csv
        wtr.nextValues("Id", "y"); //Write the column names

        CSVWriter testWtr = new CSVWriter(Objects.requireNonNull(Main.class.getResource("/")).getFile() + "test_output.csv"); //Write to output.csv
        testWtr.nextValues("Id", "x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9", "x10"); //Write the column names

        CSVReader rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/test.csv")).getFile()); //Read from test.csv
        rdr.nextLine(); //Consume the first line (column headers)
        wtr.nextRecord(); //Advance to the next line
        testWtr.nextRecord();
        int id;
        BigDecimal acc, value;
        for (int i = 0; i < 2000; ++i) { //Read all 2000 test samples
            id = rdr.nextInt(); //Read the id
            System.out.println(id);
            wtr.nextInt(id); //Write the id
            testWtr.nextInt(id);
            acc = BigDecimal.ZERO;
            for (int j = 0; j < 10; ++j) { //Read all 10 x_i values that are averaged over
                value = rdr.nextBigDecimal();
                System.out.print(value.stripTrailingZeros().toPlainString() + " + ");
                testWtr.nextBigDecimal(value);
                acc = acc.add(value); //Accumulate the value
            }
            acc = acc.divide(BigDecimal.TEN, 100, RoundingMode.HALF_EVEN);
            System.out.println(/*"= " + acc.stripTrailingZeros().toPlainString()*/);
            wtr.nextBigDecimal(acc);
            wtr.nextRecord(); //Advance to the next line
            testWtr.nextRecord();
        }
        rdr.close(); //Close the reader

        wtr.close(); //Close the writer
        testWtr.close();
    }
}
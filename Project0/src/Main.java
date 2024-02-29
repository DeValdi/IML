import java.util.Objects;

public class Main {
    public static void main(String[] args) {
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

        CSVWriter wtr = new CSVWriter(Objects.requireNonNull(Main.class.getResource("/")).getFile() + "output.csv"); //Write to output.csv
        wtr.nextValues("Id", "y"); //Write the column names

        rdr = new CSVReader(Objects.requireNonNull(Main.class.getResource("/test.csv")).getFile()); //Read from test.csv
        rdr.nextLine(); //Consume the first line (column headers)
        double[] xData = new double[10]; //The data for the x vector
        Matrix x = new Matrix(10, 1, xData); //The x vector in transposed form for multiplication
        for (int i = 0; i < 2000; ++i) { //Read all 2000 test samples
            wtr.nextRecord(); //Advance to the next line
            wtr.nextInt(rdr.nextInt()); //Write the id
            for (int j = 0; j < 10; ++j) //Read all 10 x_i values that are averaged over
                xData[j] = rdr.nextDouble(); //Store the x_i into the x vector
            wtr.nextDouble(w.dot(x)); //Write the computed average
        }
        rdr.close();

        wtr.close(); //Close the writer
    }
}
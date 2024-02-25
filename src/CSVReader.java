import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
    private final BufferedReader reader;

    public CSVReader(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int nextInt() {
        try {
            int sign;
            int curr = this.reader.read();
            if (curr == '-') {
                sign = -1;
                curr = this.reader.read();
            } else
                sign = 1;
            int value = curr - '0';
            curr = this.reader.read();
            while (curr != '.' && curr != ',' && curr != '\n' && curr != -1) {
                value = 10 * value + (curr - '0');
                curr = this.reader.read();
            }
            return sign * value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double nextDouble() {
        try {
            double sign;
            int curr = this.reader.read();
            if (curr == '-') {
                sign = -1;
                curr = this.reader.read();
            } else
                sign = 1;
            double value = curr - '0';
            curr = this.reader.read();
            while (curr != '.' && curr != ',' && curr != '\n' && curr != -1) {
                value = 10d * value + (curr - '0');
                curr = this.reader.read();
            }
            if (curr == '.') {
                double fPart = 0;
                double fScale = 1;
                curr = this.reader.read();
                while (curr != ',' && curr != '\n' && curr != -1) {
                    fPart = 10d * fPart + (curr - '0');
                    fScale *= 10d;
                    curr = this.reader.read();
                }
                value += fPart / fScale;
            }
            return sign * value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

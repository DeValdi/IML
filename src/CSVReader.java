import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * An optimized reader for reading from .csv files.
 */
public class CSVReader {
    private final BufferedReader reader;

    /**
     * <p>
     *     Create a new reader from the given {@code path}.
     * </p>
     * <p>
     *     Throws a {@link RuntimeException} if no file is found at the path.
     * </p>
     * @param path The file name
     */
    public CSVReader(String path) {
        try {
            this.reader = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     *     Consumes the next line and returns it.
     * </p>
     * <p>
     *     May be used to skip the column headers normally found on the first line.
     * </p>
     * @return The next line
     */
    public String nextLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Consumes the next {@code int} and returns it.</p>
     * <p>
     *     It is assumed that the value read can fit in the {@code int} type.
     *     No checking is performed during or after the parsing of the value.
     * </p>
     * @return The next {@code int} value.
     */
    public int nextInt() {
        try {
            int sign;
            int curr = this.reader.read();
            if (curr == '-') {
                sign = -1;
                curr = this.reader.read();
            } else {
                sign = 1;
                if (curr == '+')
                    curr = this.reader.read();
            }
            int value = 0;
            while (curr != ',' && curr != '\n' && curr != -1) {
                value = 10 * value + (curr - '0');
                curr = this.reader.read();
            }
            return sign * value;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Consumes the next {@code double} and returns it.</p>
     * <p>
     *     It is assumed that the value read can fit in the {@code double} type.
     *     No checking is performed during or after the parsing of the value.
     * </p>
     * @return The next {@code double} value.
     */
    public double nextDouble() {
        try {
            double sign;
            int curr = this.reader.read();
            if (curr == '-') {
                sign = -1d;
                curr = this.reader.read();
            } else {
                sign = 1d;
                if (curr == '+')
                    curr = this.reader.read();
            }
            double value = 0d;
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

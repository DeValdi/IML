import java.io.FileWriter;
import java.io.IOException;

/**
 * An optimized writer for writing to .csv files.
 */
public class CSVWriter {
    private final FileWriter writer;
    private boolean firstValue = false;
    private String record = "";

    /**
     * <p>
     *     Create a new writer from the given {@code path}.
     * </p>
     * <p>
     *     Throws a {@link RuntimeException} if the file is a directory or cannot be created.
     * </p>
     * @param path The file name
     */
    public CSVWriter(String path) {
        try {
            this.writer = new FileWriter(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>
     *     Write some values separated by commas.
     * </p>
     * <p>
     *     May be called for the column names.
     * </p>
     * @param values The values
     */
    public void nextValues(String... values) {
        this.record += String.join(",", values);
        this.firstValue = false;
    }

    /**
     * <p>
     *     Writes the current record to the file and starts a new record.
     * </p>
     */
    public void nextRecord() {
        try {
            writer.write(this.record + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.firstValue = true;
        this.record = "";
    }

    /**
     * <p>
     *     Writes an {@code int} to the file.
     * </p>
     * @param value The {@code int} value
     */
    public void nextInt(int value) {
        if (this.firstValue)
            this.firstValue = false;
        else
            this.record += ',';
        this.record += value;
    }

    /**
     * <p>
     *     Writes an {@code int} to the file.
     * </p>
     * @param value The {@code int} value
     */
    public void nextDouble(double value) {
        if (this.firstValue)
            this.firstValue = false;
        else
            this.record += ',';
        this.record += value;
    }

    /**
     * <p>
     *     Closes the writer.
     * </p>
     */
    public void close() {
        try {
            this.writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

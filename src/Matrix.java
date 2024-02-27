import java.util.*;

/**
 * An M x N matrix with entries of type {@code double}.
 */
public class Matrix {
    private static final Map<Integer, Deque<double[]>> bufs = new HashMap<>();
    private final int m;
    private final int n;
    private final int mn;
    private final double[] data;

    /**
     * <p>
     *     Creates a new M x N matrix with the backing data array in column-major order.
     * </p>
     * <p>
     *     The data array is copied by reference and NOT cloned.
     * </p>
     * @param m The row count of the matrix
     * @param n The column count of the matrix
     * @param data The backing data array in column-major order
     * @throws RuntimeException If the data size does not match the dimensions
     */
    public Matrix(int m, int n, double[] data) {
        int mn = m * n;
        if (data.length != m * n)
            throw new RuntimeException("Wrong data size");
        this.m = m;
        this.n = n;
        this.mn = mn;
        this.data = data;
    }

    /**
     * <p>
     *     Creates a new M x N zero-initialized matrix.
     * </p>
     * <p>
     *     This method allocates a data buffer.
     * </p>
     * @param m The row count of the matrix
     * @param n The column count of the matrix
     */
    public Matrix(int m, int n) {
        this(m, n, new double[m * n]);
    }

    /**
     * <p>
     *     Prints information about the usage of buffers to {@code System.out}.
     * </p>
     */
    public static void printSystemInfo() {
        for (Map.Entry<Integer, Deque<double[]>> e : bufs.entrySet())
            System.out.println("Size " + e.getKey() + " matrices: " + e.getValue().size() + " buffer(s)");
    }

    private static double[] popBuf(int mn) {
        Deque<double[]> stack = bufs.computeIfAbsent(mn, ArrayDeque::new);
        if (stack.isEmpty())
            return new double[mn];
        return stack.pop();
    }

    private static double[] popBuf(Matrix mat) {
        double[] buf = popBuf(mat.mn);
        System.arraycopy(mat.data, 0, buf, 0, mat.mn);
        return buf;
    }

    private static void pushBuf(double[] buf) {
        bufs.get(buf.length).push(buf);
    }

    private static void pushBuf(double[] buf, Matrix mat) {
        System.arraycopy(buf, 0, mat.data, 0, mat.mn);
        pushBuf(buf);
    }

    private static void assertSameSize(Matrix a, Matrix b) {
        if (a.m != b.m || a.n != b.n)
            throw new RuntimeException("Matrices are of wrong size");
    }

    private static void assertTransposedSize(Matrix a, Matrix b) {
        if (a.m != b.n || a.n != b.m)
            throw new RuntimeException("Matrices are of wrong size");
    }

    private static void assertSquare(Matrix a) {
        if (a.m != a.n)
            throw new RuntimeException("Matrix is not square");
    }

    private static void assertMultipliable(Matrix a, Matrix b) {
        if (a.n != b.m)
            throw new RuntimeException("Matrices do not agree in width / height");
    }

    private static void assertProductOf(Matrix a, Matrix b, Matrix c) {
        if (c.m != a.m || c.n != b.n)
            throw new RuntimeException("Matrix cannot store result of multiplication");
    }

    /**
     * <p>
     *     Creates a textual representation of {@code this}.
     * </p>
     * @return String with elements in scientific notation
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int index;
        for (int i = 0; i < this.m; ++i) {
            index = i;
            for (int j = 0; j < this.n; ++j) {
                str.append(String.format("% 6.3e ", this.data[index]));
                index += this.m;
            }
            str.append('\n');
        }
        return str.toString();
    }

    /**
     * <p>
     *     Turns {@code this} into an identity matrix.
     * </p>
     * <p>
     *     Additional rows or columns after the upper left block are zeroed.
     * </p>
     * @return {@code this}
     */
    public Matrix identity() {
        Arrays.fill(this.data, 0d);
        int min = Math.min(this.m, this.n), index = 0;
        for (int i = 0; i < min; ++i, index += this.m + 1)
            this.data[index] = 1d;
        return this;
    }

    /*public Matrix add(Matrix other, Matrix dest) {
        assertSameSize(this, other);
        assertSameSize(other, dest);
        for (int i = 0; i < this.mn; ++i)
            dest.data[i] = this.data[i] + other.data[i];
        return dest;
    }

    public Matrix add(Matrix other) {
        return this.add(other, this);
    }*/

    /**
     * Stores the transposed version of {@code this} into {@code dest}.
     * @param dest The destination matrix, which may be the same matrix as {@code this}
     * @return {@code dest}
     * @throws RuntimeException If {@code dest} cannot hold the result due to its dimensions
     */
    public Matrix transpose(Matrix dest) {
        assertTransposedSize(this, dest);
        double[] buf = popBuf(dest.mn);
        int thisIndex = 0;
        int destIndex = 0;
        for (int i = 0; i < this.m; ++i) {
            for (int j = 0; j < this.n; ++j) {
                buf[destIndex] = this.data[thisIndex];
                thisIndex += this.m;
                ++destIndex;
            }
            thisIndex += 1 - this.mn;
        }
        pushBuf(buf, dest);
        return dest;
    }

    /**
     * <p>
     *     Multiplies {@code this} with {@code other} and stores the result in {@code dest}.
     * </p>
     * <p>
     *     All operands may be the same object.
     * </p>
     * @param other The right operand of the multiplication
     * @param dest The destination matrix
     * @return {@code dest}
     * @throws RuntimeException If any matrix does not have the proper dimensions
     */
    public Matrix mul(Matrix other, Matrix dest) {
        assertMultipliable(this, other);
        assertProductOf(this, other, dest);
        double[] destBuf = popBuf(dest.mn);
        int thisIndex = 0, otherIndex = 0, destIndex = 0;
        double acc;
        for (int j = 0; j < dest.n; ++j) {
            for (int i = 0; i < dest.m; ++i) {
                acc = 0;
                for (int k = 0; k < this.n; ++k) {
                    acc += this.data[thisIndex] * other.data[otherIndex];
                    thisIndex += this.m; //Advance left matrix to the right
                    ++otherIndex; //Advance right matrix to the bottom
                }
                thisIndex += 1 - this.mn; //Reset left matrix to the left and move one down
                otherIndex -= other.m; //Reset right matrix to the top
                destBuf[destIndex++] = acc;
            }
            thisIndex = 0;
            otherIndex += other.m;
        }
        pushBuf(destBuf, dest);
        return dest;
    }

    /**
     * <p>
     *     Inverts {@code this} and stores the result in {@code dest}.
     * </p>
     * @param dest The destination matrix, which may be the same matrix as {@code this}
     * @return {@code dest}
     * @throws RuntimeException If {@code this} is not square or singular
     * or if {@code dest} cannot hold the result due to its dimensions
     */
    public Matrix invert(Matrix dest) {
        assertSquare(this);
        assertSameSize(this, dest);
        double[] lhs = popBuf(this);
        double[] rhs = popBuf(dest.identity());
        int i, j, k, pI = 0, sI, pJ, sJ;
        double temp, factor;
        for (i = 0; i < this.m; ++i) {
            sI = pI; //Set swap row to pivot row
            while (Math.abs(lhs[sI]) < 0.000001) { //TODO rel. error
                ++sI; //Try other rows while degenerate
                if (i + sI - pI == this.m)
                    throw new RuntimeException("Matrix is singular");
            }
            if (sI != pI) { //If was degenerate at least once, we need to swap
                pJ = i; //Start a row-wise iteration at the pivot row
                sJ = i + sI - pI; //Start a row-wise iteration at the swap row
                for (j = 0; j < i; ++j) { //Go through the previous columns on the RHS
                    temp = rhs[pJ];
                    rhs[pJ] = rhs[sJ];
                    rhs[sJ] = temp;
                    pJ += this.m;
                    sJ += this.m;
                }
                for (; j < this.n; ++j) { //Go through all remaining columns and swap
                    temp = lhs[pJ];
                    lhs[pJ] = lhs[sJ];
                    lhs[sJ] = temp;
                    temp = rhs[pJ];
                    rhs[pJ] = rhs[sJ];
                    rhs[sJ] = temp;
                    pJ += this.m;
                    sJ += this.m;
                }
            }
            factor = 1/lhs[pI]; //Get the pivot factor
            pJ = i;
            for (j = 0; j < i; ++j) {
                rhs[pJ] *= factor;
                pJ += this.m;
            }
            lhs[pI] = 1d; //Set the pivot's new value to 1
            rhs[pI] *= factor; //Multiply RHS
            pJ += this.m; //Advance to the next column
            for (j = i + 1; j < this.n; ++j) { //Multiply all remaining columns with the pivot factor
                lhs[pJ] *= factor;
                rhs[pJ] *= factor;
                pJ += this.m;
            }
            sI = pI + 1; //First row after pivot
            for (k = i + 1; k < this.m; ++k) { //Loop over all remaining rows
                factor = lhs[sI]; //Get factor for row
                pJ = i;
                sJ = k;
                for (j = 0; j < i; ++j) {
                    rhs[sJ] -= factor * rhs[pJ];
                    pJ += this.m;
                    sJ += this.m;
                }
                lhs[sI] = 0d; //Set new value of eliminated column to 0
                rhs[sI] -= factor * rhs[pI];
                pJ += this.m; //Jump to second column
                sJ += this.m; //Jump to second column
                for (j = i + 1; j < this.n; ++j) { //Loop over all remaining columns
                    lhs[sJ] -= factor * lhs[pJ];
                    rhs[sJ] -= factor * rhs[pJ];
                    pJ += this.m;
                    sJ += this.m;
                }
                ++sI;
            }
            pI = sI + i + 1;
        }
        pI = this.mn - 1; //Set last element as pivot
        for (i = this.m - 1; i >= 0; --i) { //Loop over all rows backwards
            sI = pI - 1;
            for (k = i - 1; k >= 0; --k) { //Loop over the remaining rows backwards
                factor = lhs[sI];
                lhs[sI] = 0;
                pJ = i;
                sJ = k;
                for (j = 0; j < this.n; ++j) {
                    rhs[sJ] -= factor * rhs[pJ];
                    sJ += this.m;
                    pJ += this.m;
                }
                --sI;
            }
            pI -= this.m + 1;
        }
        pushBuf(rhs, dest);
        pushBuf(lhs);
        return dest;
    }

    /**
     * Inverts {@code this} and stores the result in {@code this}.
     * @return {@code this}
     * @throws RuntimeException If {@code this} is not square or singular
     */
    public Matrix invert() {
        return this.invert(this);
    }
}

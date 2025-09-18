import java.io.*;

public class RunLength {
    private static final int R    = 256;
    private static final int LG_R = 8;

    // Do not instantiate.
    private RunLength() { }

    /**
     * Reads a sequence of bits from standard input (that are encoded
     * using run-length encoding with 8-bit run lengths); decodes them;
     * and writes the results to the specified output file.
     * @param outputFilename the name of the output file
     */
    public static void expand(String outputFilename) {
        BinaryOut out = new BinaryOut(outputFilename);
        boolean b = false;
        while (!BinaryStdIn.isEmpty()) {
            int run = BinaryStdIn.readInt(LG_R);
            for (int i = 0; i < run; i++)
                out.write(b);
            b = !b;
        }
        out.close();
    }

    /**
     * Reads a sequence of bits from standard input; compresses
     * them using run-length coding with 8-bit run lengths; and writes the
     * results to the specified output file.
     * @param outputFilename the name of the output file
     */
    public static void compress(String outputFilename) {
        BinaryOut out = new BinaryOut(outputFilename);
        char run = 0;
        boolean old = false;
        while (!BinaryStdIn.isEmpty()) {
            boolean b = BinaryStdIn.readBoolean();
            if (b != old) {
                out.write(run, LG_R);
                run = 1;
                old = !old;
            }
            else {
                if (run == R-1) {
                    out.write(run, LG_R);
                    run = 0;
                    out.write(run, LG_R);
                }
                run++;
            }
        }
        out.write(run, LG_R);
        out.close();
    }

    /**
     * Sample client that calls {@code compress()} or {@code expand()}
     * based on the command-line argument, reading from input file and writing to output file.
     *
     * @param args the command-line arguments: "-" or "+" followed by input file and output file
     */
    public static void main(String[] args) throws IOException {
        // Debug: In số lượng tham số và nội dung tham số
        System.out.println("Debug: Number of arguments: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("Debug: Argument " + i + ": " + args[i]);
        }

        // Kiểm tra số lượng tham số
        if (args.length != 3) {
            System.err.println("Error: Expected 3 arguments, got " + args.length);
            throw new IllegalArgumentException("Usage: java RunLength [-/+] inputFile outputFile");
        }

        // Debug: In tên tệp đầu vào
        System.out.println("Debug: Attempting to read input file: " + args[1]);
        File inputFile = new File(args[1]);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("Error: Input file '" + args[1] + "' does not exist or is not a file");
            throw new IllegalArgumentException("Input file '" + args[1] + "' does not exist or is not a file");
        }

        // Thiết lập luồng đầu vào
        System.setIn(new FileInputStream(inputFile));
        System.out.println("Debug: Input stream set to file: " + args[1]);

        // Debug: In chế độ hoạt động và tệp đầu ra
        System.out.println("Debug: Mode: " + args[0] + ", Output file: " + args[2]);
        if (args[0].equals("-")) {
            System.out.println("Debug: Starting compression...");
            compress(args[2]);
            System.out.println("Debug: Compression completed, output written to: " + args[2]);
        }
        else if (args[0].equals("+")) {
            System.out.println("Debug: Starting decompression...");
            expand(args[2]);
            System.out.println("Debug: Decompression completed, output written to: " + args[2]);
        }
        else {
            System.err.println("Error: Invalid mode '" + args[0] + "'");
            throw new IllegalArgumentException("Illegal command line argument: must be '-' or '+'");
        }
    }
}
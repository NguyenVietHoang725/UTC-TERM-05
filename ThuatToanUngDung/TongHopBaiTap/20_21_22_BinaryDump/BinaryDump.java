import java.io.*;

public class BinaryDump {

    // Do not instantiate.
    private BinaryDump() { }

    /**
     * Reads in a sequence of bytes from a specified file and writes
     * them to standard output in binary, k bits per line,
     * where k is given as the first command-line argument; also writes the number
     * of bits.
     *
     * @param args the command-line arguments: bitsPerLine inputFile
     */
    public static void main(String[] args) throws IOException {
        // Kiểm tra số lượng tham số
        if (args.length != 2) {
            System.err.println("Error: Expected 2 arguments (bitsPerLine inputFile), got " + args.length);
            throw new IllegalArgumentException("Usage: java BinaryDump bitsPerLine inputFile");
        }

        // Lấy số bit mỗi dòng
        int bitsPerLine;
        try {
            bitsPerLine = Integer.parseInt(args[0]);
            if (bitsPerLine < 0) {
                System.err.println("Error: bitsPerLine must be non-negative, got " + args[0]);
                throw new IllegalArgumentException("bitsPerLine must be non-negative");
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: First argument must be an integer, got '" + args[0] + "'");
            throw new IllegalArgumentException("First argument must be an integer");
        }

        // Lấy tên tệp đầu vào và kiểm tra
        String inputFile = args[1];
        File file = new File(inputFile);
        if (!file.exists() || !file.isFile()) {
            System.err.println("Error: Input file '" + inputFile + "' does not exist or is not a file");
            throw new IllegalArgumentException("Input file '" + inputFile + "' does not exist or is not a file");
        }

        // Thiết lập luồng đầu vào
        System.setIn(new FileInputStream(file));
        System.out.println("Debug: Reading binary data from file: " + inputFile);

        // Đọc và hiển thị các bit
        int count;
        for (count = 0; !BinaryStdIn.isEmpty(); count++) {
            if (bitsPerLine == 0) {
                BinaryStdIn.readBoolean();
                continue;
            }
            if (count != 0 && count % bitsPerLine == 0) StdOut.println();
            try {
                if (BinaryStdIn.readBoolean()) StdOut.print(1);
                else StdOut.print(0);
            } catch (RuntimeException e) {
                System.err.println("Error: Failed to read bit at position " + count + ": " + e.getMessage());
                break;
            }
        }

        if (bitsPerLine != 0) StdOut.println();
        System.out.println("Debug: Total bits read in binary format: " + count);
        StdOut.println(count + " bits");
    }
}
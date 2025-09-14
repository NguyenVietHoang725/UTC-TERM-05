/**
 * Simplified RunLength class for text file compression/decompression.
 * Extends the original RunLength functionality to compress .txt files to .bin
 * and decompress .bin files back to .txt using BinaryOut.
 * 
 * Usage:
 * - java EnhancedRunLength -c input.txt output.bin    (compress txt to bin)
 * - java EnhancedRunLength -d input.bin output.txt    (decompress bin to txt)
 * 
 * @author Your Name
 */
public class EnhancedRunLength extends RunLength {
    private static final int R = 256;
    private static final int LG_R = 8;

    // Do not instantiate directly
    private EnhancedRunLength() { }

    /**
     * Compresses a text file to binary format using run-length encoding.
     * 
     * @param inputTxtFile  path to the input .txt file
     * @param outputBinFile path to the output .bin file
     */
    public static void compressTextToBinary(String inputTxtFile, String outputBinFile) {
        BinaryIn input = new BinaryIn(inputTxtFile);
        BinaryOut output = new BinaryOut(outputBinFile);
        
        char run = 0;
        boolean old = false;
        
        while (!input.isEmpty()) {
            boolean b = input.readBoolean();
            if (b != old) {
                output.write(run, LG_R);
                run = 1;
                old = !old;
            }
            else {
                if (run == R-1) {
                    output.write(run, LG_R);
                    run = 0;
                    output.write(run, LG_R);
                }
                run++;
            }
        }
        output.write(run, LG_R);
        output.close();
        
        System.out.println("Compressed: " + inputTxtFile + " -> " + outputBinFile);
    }

    /**
     * Decompresses a binary file back to text format.
     * 
     * @param inputBinFile  path to the input .bin file
     * @param outputTxtFile path to the output .txt file
     */
    public static void decompressBinaryToText(String inputBinFile, String outputTxtFile) {
        BinaryIn input = new BinaryIn(inputBinFile);
        BinaryOut output = new BinaryOut(outputTxtFile);
        
        boolean b = false;
        while (!input.isEmpty()) {
            int run = input.readInt(LG_R);
            for (int i = 0; i < run; i++)
                output.write(b);
            b = !b;
        }
        output.close();
        
        System.out.println("Decompressed: " + inputBinFile + " -> " + outputTxtFile);
    }

    /**
     * Gets file size in bytes.
     * 
     * @param filename path to the file
     * @return file size in bytes
     */
    private static long getFileSize(String filename) {
        java.io.File file = new java.io.File(filename);
        return file.exists() ? file.length() : 0;
    }

    /**
     * Compresses text file with compression statistics.
     * 
     * @param inputTxtFile  path to the input .txt file
     * @param outputBinFile path to the output .bin file
     */
    public static void compressWithStats(String inputTxtFile, String outputBinFile) {
        long originalSize = getFileSize(inputTxtFile);
        
        compressTextToBinary(inputTxtFile, outputBinFile);
        
        long compressedSize = getFileSize(outputBinFile);
        double ratio = originalSize > 0 ? (double) compressedSize / originalSize * 100.0 : 0.0;
        
        System.out.printf("Original size: %d bytes\n", originalSize);
        System.out.printf("Compressed size: %d bytes\n", compressedSize);
        System.out.printf("Compression ratio: %.2f%%\n", ratio);
        System.out.printf("Space saved: %.2f%%\n", 100.0 - ratio);
    }

    /**
     * Main method with simple command line interface.
     * 
     * Usage:
     * java EnhancedRunLength -c input.txt output.bin    (compress)
     * java EnhancedRunLength -d input.bin output.txt    (decompress)
     * java EnhancedRunLength -cs input.txt output.bin   (compress with stats)
     * 
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage:");
            System.out.println("  java EnhancedRunLength -c input.txt output.bin    (compress txt to bin)");
            System.out.println("  java EnhancedRunLength -d input.bin output.txt    (decompress bin to txt)");
            System.out.println("  java EnhancedRunLength -cs input.txt output.bin   (compress with statistics)");
            return;
        }

        String mode = args[0];
        String inputFile = args[1];
        String outputFile = args[2];

        // Validate input file exists
        java.io.File input = new java.io.File(inputFile);
        if (!input.exists()) {
            System.err.println("Error: Input file '" + inputFile + "' does not exist.");
            return;
        }

        try {
            switch (mode) {
                case "-c":
                    // Compress txt to bin
                    if (!inputFile.toLowerCase().endsWith(".txt")) {
                        System.out.println("Warning: Input file doesn't have .txt extension");
                    }
                    if (!outputFile.toLowerCase().endsWith(".bin")) {
                        System.out.println("Warning: Output file doesn't have .bin extension");
                    }
                    compressTextToBinary(inputFile, outputFile);
                    break;

                case "-d":
                    // Decompress bin to txt
                    if (!inputFile.toLowerCase().endsWith(".bin")) {
                        System.out.println("Warning: Input file doesn't have .bin extension");
                    }
                    if (!outputFile.toLowerCase().endsWith(".txt")) {
                        System.out.println("Warning: Output file doesn't have .txt extension");
                    }
                    decompressBinaryToText(inputFile, outputFile);
                    break;

                case "-cs":
                    // Compress with statistics
                    if (!inputFile.toLowerCase().endsWith(".txt")) {
                        System.out.println("Warning: Input file doesn't have .txt extension");
                    }
                    if (!outputFile.toLowerCase().endsWith(".bin")) {
                        System.out.println("Warning: Output file doesn't have .bin extension");
                    }
                    compressWithStats(inputFile, outputFile);
                    break;

                default:
                    System.err.println("Error: Invalid mode '" + mode + "'");
                    System.out.println("Use -c for compress, -d for decompress, -cs for compress with stats");
            }
        } catch (Exception e) {
            System.err.println("Error processing file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
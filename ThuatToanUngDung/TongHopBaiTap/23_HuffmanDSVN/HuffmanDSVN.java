/******************************************************************************
 *  Compilation:  javac HuffmanDSVN.java
 *  Execution:    java HuffmanDSVN - input.txt output.bin   (compress)
 *  Execution:    java HuffmanDSVN + input.bin output.txt   (expand)
 *  Dependencies: MinPQ.java
 *  Description:  Compresses or expands Vietnamese text (UTF-8, comma-separated names)
 *                using Huffman coding with Unicode 16-bit support.
 *                Visualizes Huffman trie during compression.
 *
 *  Example input: Đỗ Văn An,Lương Hoài An,Nguyễn Hoàng An,Lê Hoàng Anh,...
 *
 *  % java HuffmanDSVN - input.txt output.bin
 *  % java HuffmanDSVN + output.bin output.txt
 *
 ******************************************************************************/

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The {@code HuffmanDSVN} class extends {@code Huffman} to support compression
 * and expansion of Vietnamese text (Unicode characters) using Huffman codes.
 * It reads UTF-8 encoded input, visualizes the Huffman trie, and handles 16-bit
 * characters to support Vietnamese diacritics.
 */
public class HuffmanDSVN extends Huffman {

    // Alphabet size for Unicode (16-bit)
    private static final int R = 65536;

    /**
     * Compresses the input string from a UTF-8 encoded file, builds and prints
     * the Huffman trie, and writes the compressed binary to output file.
     */
    public static void compress(String inputFile, String outputFile) throws IOException {
        // Read input as UTF-8 string
        byte[] bytes = Files.readAllBytes(Paths.get(inputFile));
        String s = new String(bytes, StandardCharsets.UTF_8);
        char[] input = s.toCharArray();

        // Tabulate frequency counts
        int[] freq = new int[R];
        for (char c : input) {
            freq[c]++;
        }

        // Build Huffman trie
        Node root = buildTrie(freq);

        // Print input and stats
        System.out.println("Input string: " + s);
        System.out.println("Number of unique chars: " + countUnique(freq));
        System.out.println("Trie root freq: " + root.freq);

        // Print trie for visualization
        System.out.println("Huffman Trie Visualization:");
        printTrie(root, "", true);

        // Build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // Write compressed data
        try (BitOutputStream out = new BitOutputStream(new FileOutputStream(outputFile))) {
            // Write trie
            writeTrie(root, out);

            // Write length of input
            out.writeInt(input.length);

            // Encode input
            for (char c : input) {
                String code = st[c];
                for (char bit : code.toCharArray()) {
                    out.writeBit(bit == '1');
                }
            }
        }
    }

    /**
     * Expands a compressed binary input file, writing the decoded UTF-8 string
     * to the output file.
     */
    public static void expand(String inputFile, String outputFile) throws IOException {
        try (BitInputStream in = new BitInputStream(new FileInputStream(inputFile))) {
            // Read Huffman trie
            Node root = readTrie(in);

            // Read length of original message
            int length = in.readInt();

            // Decode
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < length; i++) {
                Node x = root;
                while (!x.isLeaf()) {
                    boolean bit = in.readBit();
                    x = bit ? x.right : x.left;
                }
                sb.append(x.ch);
            }

            // Write to output file as UTF-8
            Files.write(Paths.get(outputFile), sb.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    // Modified writeTrie for 16-bit chars
    private static void writeTrie(Node x, BitOutputStream out) throws IOException {
        if (x.isLeaf()) {
            out.writeBit(true);
            out.writeChar16(x.ch);
            return;
        }
        out.writeBit(false);
        writeTrie(x.left, out);
        writeTrie(x.right, out);
    }

    // Modified readTrie for 16-bit chars
    private static Node readTrie(BitInputStream in) throws IOException {
        boolean isLeaf = in.readBit();
        if (isLeaf) {
            char ch = in.readChar16();
            return new Node(ch, -1, null, null);
        } else {
            return new Node('\0', -1, readTrie(in), readTrie(in));
        }
    }

    // Visualize the Huffman trie
    private static void printTrie(Node x, String prefix, boolean isLeft) {
        if (x != null) {
            String charDisplay = x.isLeaf() ? "'" + (x.ch == '\0' ? "\\0" : x.ch) + "' (freq: " + x.freq + ")" : "(freq: " + x.freq + ")";
            System.out.println(prefix + (isLeft ? "├── " : "└── ") + charDisplay);
            if (!x.isLeaf()) {
                printTrie(x.left, prefix + (isLeft ? "│   " : "    "), true);
                printTrie(x.right, prefix + (isLeft ? "│   " : "    "), false);
            }
        }
    }

    // Override buildTrie to use larger R
    protected static Node buildTrie(int[] freq) {
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char c = 0; c < R; c++) {
            if (freq[c] > 0) {
                pq.insert(new Node(c, freq[c], null, null));
            }
        }
        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        return pq.delMin();
    }

    private static int countUnique(int[] freq) {
        int count = 0;
        for (int f : freq) {
            if (f > 0) count++;
        }
        return count;
    }

    /**
     * Main method similar to Huffman's, with UTF-8 input handling.
     */
    public static void main(String[] args) throws IOException {
        // Debug: Print number and content of arguments
        System.out.println("Debug: Number of arguments: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("Debug: Argument " + i + ": " + args[i]);
        }

        // Check number of arguments
        if (args.length != 3) {
            System.err.println("Error: Expected 3 arguments, got " + args.length);
            throw new IllegalArgumentException("Usage: java HuffmanDSVN [-/+] inputFile outputFile");
        }

        // Debug: Print input file
        System.out.println("Debug: Attempting to read input file: " + args[1]);
        File inputFile = new File(args[1]);
        if (!inputFile.exists() || !inputFile.isFile()) {
            System.err.println("Error: Input file '" + args[1] + "' does not exist or is not a file");
            throw new IllegalArgumentException("Input file '" + args[1] + "' does not exist or is not a file");
        }

        // Debug: Print mode and output file
        System.out.println("Debug: Mode: " + args[0] + ", Output file: " + args[2]);
        if (args[0].equals("-")) {
            System.out.println("Debug: Starting compression...");
            compress(args[1], args[2]);
            System.out.println("Debug: Compression completed, output written to: " + args[2]);
        } else if (args[0].equals("+")) {
            System.out.println("Debug: Starting decompression...");
            expand(args[1], args[2]);
            System.out.println("Debug: Decompression completed, output written to: " + args[2]);
        } else {
            System.err.println("Error: Invalid mode '" + args[0] + "'");
            throw new IllegalArgumentException("Illegal command line argument: must be '-' or '+'");
        }
    }

    // Helper class for bit-level output
    private static class BitOutputStream implements AutoCloseable {
        private OutputStream out;
        private int buffer;
        private int bitsInBuffer;

        public BitOutputStream(OutputStream out) {
            this.out = out;
            this.buffer = 0;
            this.bitsInBuffer = 0;
        }

        public void writeBit(boolean bit) throws IOException {
            buffer = (buffer << 1) | (bit ? 1 : 0);
            bitsInBuffer++;
            if (bitsInBuffer == 8) {
                out.write(buffer);
                buffer = 0;
                bitsInBuffer = 0;
            }
        }

        public void writeChar16(char x) throws IOException {
            out.write((x >>> 8) & 0xFF);
            out.write(x & 0xFF);
        }

        public void writeInt(int x) throws IOException {
            out.write((x >>> 24) & 0xFF);
            out.write((x >>> 16) & 0xFF);
            out.write((x >>> 8) & 0xFF);
            out.write(x & 0xFF);
        }

        public void close() throws IOException {
            if (bitsInBuffer > 0) {
                buffer <<= (8 - bitsInBuffer);
                out.write(buffer);
            }
            out.close();
        }
    }

    // Helper class for bit-level input
    private static class BitInputStream implements AutoCloseable {
        private InputStream in;
        private int buffer;
        private int bitsInBuffer;
        private boolean eof;

        public BitInputStream(InputStream in) {
            this.in = in;
            this.buffer = 0;
            this.bitsInBuffer = 0;
            this.eof = false;
        }

        public boolean readBit() throws IOException {
            if (eof) throw new EOFException("End of input stream");
            if (bitsInBuffer == 0) {
                int nextByte = in.read();
                if (nextByte == -1) {
                    eof = true;
                    throw new EOFException("End of input stream");
                }
                buffer = nextByte;
                bitsInBuffer = 8;
            }
            bitsInBuffer--;
            return ((buffer >>> bitsInBuffer) & 1) == 1;
        }

        public char readChar16() throws IOException {
            int high = in.read();
            int low = in.read();
            if (high == -1 || low == -1) throw new EOFException("End of input stream");
            return (char) ((high << 8) | low);
        }

        public int readInt() throws IOException {
            int x = 0;
            for (int i = 0; i < 4; i++) {
                int b = in.read();
                if (b == -1) throw new EOFException("End of input stream");
                x = (x << 8) | b;
            }
            return x;
        }

        public void close() throws IOException {
            in.close();
        }
    }
}
/**
 * The {@code KMPFile} class extends {@code KMP} to support Vietnamese text search
 * from a .docx file using the {@code VietnameseAlphabet}.
 * It reads text from a .docx file and searches for a pattern of approximately 30 characters.
 */
import java.io.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class KMPFile extends KMP {
    private final Alphabet alphabet;

    /**
     * Preprocesses the pattern string using the specified alphabet.
     *
     * @param pat the pattern string (approximately 30 characters)
     * @param alphabet the alphabet to use for mapping characters to indices
     */
    public KMPFile(String pat, Alphabet alphabet) {
        super(toIndexedCharArray(pat, alphabet), alphabet.radix());
        this.alphabet = alphabet;
    }

    /**
     * Converts a string to a char array where each char represents the index in the alphabet.
     *
     * @param s the string to convert
     * @param alphabet the alphabet for mapping
     * @return the char array of indices
     */
    private static char[] toIndexedCharArray(String s, Alphabet alphabet) {
        char[] indexed = new char[s.length()];
        for (int i = 0; i < s.length(); i++) {
            indexed[i] = (char) alphabet.toIndex(s.charAt(i));
        }
        return indexed;
    }

    /**
     * Reads text from a .docx file.
     *
     * @param filePath the path to the .docx file
     * @return the text content of the file
     * @throws IOException if there is an error reading the file
     */
    private String readDocxFile(String filePath) throws IOException {
        StringBuilder textBuilder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    textBuilder.append(text).append(" ");
                }
            }
        }
        // Standardize text by replacing multiple spaces with a single space
        return textBuilder.toString().replaceAll("\\s+", " ");
    }

    /**
     * Searches for the pattern in the text read from a .docx file.
     *
     * @param filePath the path to the .docx file
     * @return the index of the first occurrence of the pattern in the text; -1 if not found or error
     */
    public int searchInFile(String filePath) {
        try {
            String text = readDocxFile(filePath);
            return super.search(toIndexedCharArray(text, alphabet));
        } catch (IOException e) {
            System.err.println("Error reading .docx file: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Main method for testing with a Vietnamese pattern and a .docx file.
     * Example: Pattern of approximately 30 characters.
     *
     * @param args command-line arguments (pattern and file path)
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java KMPFile <pattern> <file.docx>");
            return;
        }
        String pat = args[0]; // e.g., "Thuật toán KMP được sử dụng để tìm kiếm chuỗi"
        String filePath = args[1]; // e.g., "sample_tieng_viet.docx"

        KMPFile kmp = new KMPFile(pat, VietnameseAlphabet.VIETNAMESE_ALPHABET);
        int offset = kmp.searchInFile(filePath);

        if (offset >= 0) {
            try {
                String text = kmp.readDocxFile(filePath);
                StdOut.println("text:    " + text);
                StdOut.print("pattern: ");
                for (int i = 0; i < offset; i++) {
                    StdOut.print(" ");
                }
                StdOut.println(pat);
            } catch (IOException e) {
                System.err.println("Error reading .docx file for display: " + e.getMessage());
            }
        } else {
            StdOut.println("Pattern not found or error occurred.");
        }
    }
}
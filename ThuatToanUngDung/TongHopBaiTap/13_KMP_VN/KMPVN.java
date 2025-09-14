/**
 * The {@code KMPVN} class extends {@code KMP} to support Vietnamese text using the {@code VietnameseAlphabet}.
 * It maps characters to indices in the specified alphabet and performs the search on the indexed representation.
 */
public class KMPVN extends KMP {
    private final Alphabet alphabet;

    /**
     * Preprocesses the pattern string using the default Vietnamese alphabet.
     *
     * @param pat the pattern string
     */
    public KMPVN(String pat) {
        this(pat, VietnameseAlphabet.VIETNAMESE_ALPHABET);
    }

    /**
     * Preprocesses the pattern string using the specified alphabet.
     *
     * @param pat the pattern string
     * @param alphabet the alphabet to use for mapping characters to indices
     */
    public KMPVN(String pat, Alphabet alphabet) {
        super(toIndexedCharArray(pat, alphabet), alphabet.radix());
        this.alphabet = alphabet;
    }

    /**
     * Converts a string to a char array where each char represents the index in the alphabet.
     * This allows using the superclass constructor that expects indices in 0 to R-1.
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
     * Returns the index of the first occurrence of the pattern string in the text string.
     *
     * @param txt the text string
     * @return the index of the first occurrence of the pattern string in the text string; N if no such match
     */
    @Override
    public int search(String txt) {
        return super.search(toIndexedCharArray(txt, alphabet));
    }

    /**
     * Main method for testing with a Vietnamese pattern and text.
     * Example: Pattern of length around 15 characters.
     *
     * @param args command-line arguments (pattern and text)
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java KMPVN <pattern> <text>");
            return;
        }
        String pat = args[0];  // e.g., "thuật toán KMP"
        String txt = args[1];  // e.g., a longer Vietnamese text containing the pattern

        KMPVN kmp = new KMPVN(pat);
        int offset = kmp.search(txt);

        // Print results
        StdOut.println("text:    " + txt);
        StdOut.print("pattern: ");
        for (int i = 0; i < offset; i++) {
            StdOut.print(" ");
        }
        StdOut.println(pat);
    }
}
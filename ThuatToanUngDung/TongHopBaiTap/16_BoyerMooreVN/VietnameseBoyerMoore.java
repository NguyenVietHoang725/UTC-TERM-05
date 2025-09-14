/******************************************************************************
 *  Compilation:  javac VietnameseBoyerMoore.java
 *  Execution:    java VietnameseBoyerMoore pattern text
 *  Dependencies: VietnameseAlphabet.java Alphabet.java StdOut.java
 *
 *  A class that extends BoyerMoore to support string searching in Vietnamese text
 *  using the VietnameseAlphabet class. It uses the bad-character rule of the
 *  Boyer-Moore algorithm (does not implement the strong good suffix rule).
 *
 *  Example:
 *  % java VietnameseBoyerMoore "chào" "xin chào thế giới"
 *  text:    xin chào thế giới
 *  pattern:     chào
 *
 ******************************************************************************/

public class VietnameseBoyerMoore extends BoyerMoore {
    private final Alphabet alphabet; // The Vietnamese alphabet
    private int[] right;             // The bad-character skip array
    private char[] pattern;          // Store the pattern as a character array
    private String pat;              // Store the pattern as a string

    /**
     * Preprocesses the pattern string using the VietnameseAlphabet.
     *
     * @param pat the pattern string
     */
    public VietnameseBoyerMoore(String pat) {
        super(new char[0], 0); // Gọi constructor của lớp cha với giá trị rỗng
        this.alphabet = VietnameseAlphabet.VIETNAMESE_ALPHABET;
        this.R = alphabet.radix();
        this.pat = pat;
        this.pattern = pat.toCharArray();
        initialize();
    }

    /**
     * Preprocesses the pattern string with a specified alphabet.
     *
     * @param pattern the pattern string as a character array
     * @param alphabet the alphabet to use (e.g., VietnameseAlphabet)
     */
    public VietnameseBoyerMoore(char[] pattern, Alphabet alphabet) {
        super(new char[0], 0); // Gọi constructor của lớp cha với giá trị rỗng
        this.alphabet = alphabet;
        this.R = alphabet.radix();
        this.pattern = new char[pattern.length];
        for (int j = 0; j < pattern.length; j++) {
            this.pattern[j] = pattern[j];
        }
        this.pat = new String(pattern);
        initialize();
    }

    /**
     * Initializes the bad-character skip array using the VietnameseAlphabet.
     */
    private void initialize() {
        int R = alphabet.radix();
        right = new int[R];
        for (int c = 0; c < R; c++) {
            right[c] = -1;
        }
        for (int j = 0; j < pat.length(); j++) {
            if (alphabet.contains(pat.charAt(j))) {
                right[alphabet.toIndex(pat.charAt(j))] = j;
            }
        }
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string using the VietnameseAlphabet.
     *
     * @param txt the text string
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
    @Override
    public int search(String txt) {
        int m = pat.length();
        int n = txt.length();
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                char textChar = txt.charAt(i + j);
                if (pat.charAt(j) != textChar) {
                    if (alphabet.contains(textChar)) {
                        skip = Math.max(1, j - right[alphabet.toIndex(textChar)]);
                    } else {
                        skip = j + 1; // Skip past the entire pattern segment
                    }
                    break;
                }
            }
            if (skip == 0) return i; // Found
        }
        return n; // Not found
    }

    /**
     * Returns the index of the first occurrence of the pattern string
     * in the text string using the VietnameseAlphabet.
     *
     * @param text the text string as a character array
     * @return the index of the first occurrence of the pattern string
     *         in the text string; n if no such match
     */
    @Override
    public int search(char[] text) {
        int m = pattern.length;
        int n = text.length;
        int skip;
        for (int i = 0; i <= n - m; i += skip) {
            skip = 0;
            for (int j = m - 1; j >= 0; j--) {
                char textChar = text[i + j];
                if (pattern[j] != textChar) {
                    if (alphabet.contains(textChar)) {
                        skip = Math.max(1, j - right[alphabet.toIndex(textChar)]);
                    } else {
                        skip = j + 1; // Skip past the entire pattern segment
                    }
                    break;
                }
            }
            if (skip == 0) return i; // Found
        }
        return n; // Not found
    }

    /**
     * Takes a pattern string and an input string as command-line arguments;
     * searches for the pattern string in the text string; and prints
     * the first occurrence of the pattern string in the text string.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String pat = args[0];
        String txt = args[1];

        // Kiểm tra xem mẫu có chứa ký tự không được hỗ trợ không
        Alphabet alphabet = VietnameseAlphabet.VIETNAMESE_ALPHABET;
        for (char c : pat.toCharArray()) {
            if (!alphabet.contains(c)) {
                throw new IllegalArgumentException("Pattern contains unsupported character: " + c);
            }
        }

        char[] pattern = pat.toCharArray();
        char[] text = txt.toCharArray();

        VietnameseBoyerMoore boyermoore1 = new VietnameseBoyerMoore(pat);
        VietnameseBoyerMoore boyermoore2 = new VietnameseBoyerMoore(pattern, VietnameseAlphabet.VIETNAMESE_ALPHABET);
        int offset1 = boyermoore1.search(txt);
        int offset2 = boyermoore2.search(text);

        // Print results
        StdOut.println("text:    " + txt);
        StdOut.print("pattern: ");
        for (int i = 0; i < offset1; i++) {
            StdOut.print(" ");
        }
        StdOut.println(pat);

        StdOut.print("pattern: ");
        for (int i = 0; i < offset2; i++) {
            StdOut.print(" ");
        }
        StdOut.println(pat);
    }
}
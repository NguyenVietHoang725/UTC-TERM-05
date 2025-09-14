/******************************************************************************
 *  Compilation:  javac VietnameseRabinKarp.java
 *  Execution:    java VietnameseRabinKarp pattern text
 *  Dependencies: VietnameseAlphabet.java Alphabet.java StdOut.java
 *
 *  A class that extends RabinKarp to support string searching in Vietnamese text
 *  using the VietnameseAlphabet class. It uses hashing with the Rabin-Karp algorithm
 *  (Las Vegas version).
 *
 *  Example:
 *  % java VietnameseRabinKarp "chào" "xin chào thế giới"
 *  text:    xin chào thế giới
 *  pattern:     chào
 *
 ******************************************************************************/

import java.math.BigInteger;
import java.util.Random;

public class VietnameseRabinKarp extends RabinKarp {
    private final Alphabet alphabet; // The Vietnamese alphabet
    private String pat;              // The pattern string
    private long patHash;            // Pattern hash value
    private int m;                   // Pattern length
    private long q;                  // A large prime
    private int R;                   // Radix from alphabet
    private long RM;                 // R^(M-1) % q

    /**
     * Preprocesses the pattern string using the VietnameseAlphabet.
     *
     * @param pat the pattern string
     */
    public VietnameseRabinKarp(String pat) {
        super(""); // Call parent constructor with empty string to initialize safely
        this.alphabet = VietnameseAlphabet.VIETNAMESE_ALPHABET;
        this.R = alphabet.radix();
        this.pat = pat;
        this.m = pat.length();
        this.q = longRandomPrime();

        // Validate pattern
        for (char c : pat.toCharArray()) {
            if (!alphabet.contains(c)) {
                throw new IllegalArgumentException("Pattern contains unsupported character: " + c);
            }
        }

        // Precompute R^(m-1) % q
        RM = 1;
        for (int i = 1; i <= m - 1; i++) {
            RM = (R * RM) % q;
        }

        // Compute pattern hash using alphabet indices
        patHash = hash(pat, m);
    }

    /**
     * Preprocesses the pattern string with a specified alphabet.
     *
     * @param pattern the pattern string as a character array
     * @param alphabet the alphabet to use (e.g., VietnameseAlphabet)
     */
    public VietnameseRabinKarp(char[] pattern, Alphabet alphabet) {
        super(""); // Call parent constructor with empty string to initialize safely
        this.alphabet = alphabet;
        this.R = alphabet.radix();
        this.pat = new String(pattern);
        this.m = pattern.length;
        this.q = longRandomPrime();

        // Validate pattern
        for (char c : pattern) {
            if (!alphabet.contains(c)) {
                throw new IllegalArgumentException("Pattern contains unsupported character: " + c);
            }
        }

        // Precompute R^(m-1) % q
        RM = 1;
        for (int i = 1; i <= m - 1; i++) {
            RM = (R * RM) % q;
        }

        // Compute pattern hash using alphabet indices
        patHash = hash(pat, m);
    }

    // Compute hash for key[0..m-1] using alphabet indices
    private long hash(String key, int m) {
        long h = 0;
        for (int j = 0; j < m; j++) {
            char c = key.charAt(j);
            h = (R * h + alphabet.toIndex(c)) % q;
        }
        return h;
    }

    // Check if pat matches txt[i..i+m-1]
    private boolean check(String txt, int i) {
        for (int j = 0; j < m; j++) {
            if (pat.charAt(j) != txt.charAt(i + j)) {
                return false;
            }
        }
        return true;
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
        int n = txt.length();
        if (n < m) return n;

        // Compute initial text hash using alphabet indices
        long txtHash = 0;
        for (int j = 0; j < m; j++) {
            char c = txt.charAt(j);
            int index = alphabet.contains(c) ? alphabet.toIndex(c) : R - 1;
            txtHash = (R * txtHash + index) % q;
        }

        // Check for match at offset 0
        if ((patHash == txtHash) && check(txt, 0)) {
            return 0;
        }

        // Check for hash match; if hash match, check for exact match
        for (int i = m; i < n; i++) {
            // Remove leading digit
            char leadChar = txt.charAt(i - m);
            int leadIndex = alphabet.contains(leadChar) ? alphabet.toIndex(leadChar) : R - 1;
            txtHash = (txtHash + q - (RM * leadIndex) % q) % q;

            // Add trailing digit
            char trailChar = txt.charAt(i);
            int trailIndex = alphabet.contains(trailChar) ? alphabet.toIndex(trailChar) : R - 1;
            txtHash = (txtHash * R + trailIndex) % q;

            int offset = i - m + 1;
            if ((patHash == txtHash) && check(txt, offset)) {
                return offset;
            }
        }

        return n; // No match
    }

    // A random 31-bit prime (inherited from parent, but duplicated for completeness)
    private static long longRandomPrime() {
        BigInteger prime = BigInteger.probablePrime(31, new Random());
        return prime.longValue();
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

        VietnameseRabinKarp searcher = new VietnameseRabinKarp(pat);
        int offset = searcher.search(txt);

        // Print results
        StdOut.println("text:    " + txt);
        StdOut.print("pattern: ");
        for (int i = 0; i < offset; i++) {
            StdOut.print(" ");
        }
        StdOut.println(pat);
    }
}
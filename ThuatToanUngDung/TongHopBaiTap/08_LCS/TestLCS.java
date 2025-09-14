/******************************************************************************
 *  Compilation:  javac TestLCS.java
 *  Execution:    java TestLCS file1.docx file2.docx
 *  Dependencies: LongestCommonSubstring.java SuffixArrayXVN.java VietnameseAlphabet.java In.java StdOut.java org.apache.poi.xwpf.usermodel.XWPFDocument
 *
 *  Test client for LongestCommonSubstring class. Reads two .docx files,
 *  extracts their text, and finds the longest common substring between them,
 *  with support for Vietnamese characters.
 *
 ******************************************************************************/

import java.io.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *  The {@code TestLCS} class provides a test client for the {@link LongestCommonSubstring}
 *  class. It reads two .docx files specified as command-line arguments, extracts their
 *  text, and computes the longest common substring using the {@code lcs()} method.
 *  <p>
 *  This implementation supports Vietnamese text processing and handles .docx file input.
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/63suffix">Section 6.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @modified for .docx file input and Vietnamese support
 */
public class TestLCS {

    // Do not instantiate.
    private TestLCS() { }

    /**
     * Reads two .docx files from command-line arguments, extracts their text,
     * and computes the longest common substring between them.
     *
     * @param args the command-line arguments (file1.docx file2.docx)
     */
    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length != 2) {
            System.err.println("Usage: java TestLCS file1.docx file2.docx");
            return;
        }

        String file1Path = args[0];
        String file2Path = args[1];

        // Read text from first .docx file
        StringBuilder text1Builder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(file1Path));
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    text1Builder.append(text).append(" ");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + file1Path + ": " + e.getMessage());
            return;
        }

        // Read text from second .docx file
        StringBuilder text2Builder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(file2Path));
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    text2Builder.append(text).append(" ");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + file2Path + ": " + e.getMessage());
            return;
        }

        // Standardize text (replace multiple spaces with single space)
        String text1 = text1Builder.toString().trim().replaceAll("\\s+", " ");
        String text2 = text2Builder.toString().trim().replaceAll("\\s+", " ");

        // Compute the longest common substring
        String lcs = LongestCommonSubstring.lcs(text1, text2);

        // Output the result
        if (lcs.isEmpty()) {
            StdOut.println("No common substring found.");
        } else {
            StdOut.println("Longest common substring: '" + lcs + "'");
            StdOut.println("Length: " + lcs.length());
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2025, Robert Sedgewick and Kevin Wayne.
 *  Modified for .docx file input and Vietnamese support.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
/******************************************************************************
 *  Compilation:  javac KWIK.java
 *  Execution:    java KWIK file.docx context
 *  Dependencies: StdIn.java StdOut.java SuffixArrayXVN.java VietnameseAlphabet.java org.apache.poi.xwpf.usermodel.XWPFDocument
 *  Data files:   sample_tieng_viet.docx
 *
 *  Keyword-in-context search with Vietnamese alphabet support, reading from a .docx file.
 *
 ******************************************************************************/

import java.io.*;
import java.util.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 *  The {@code KWIK} class provides a {@link SuffixArrayXVN} client for computing
 *  all occurrences of a keyword in a given string from a .docx file, with surrounding context.
 *  This is known as <em>keyword-in-context search</em>.
 *  <p>
 *  For additional documentation,
 *  see <a href="https://algs4.cs.princeton.edu/63suffix">Section 6.3</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class KWIK {

    // Do not instantiate.
    private KWIK() { }

    /**
     * Reads a string from a .docx file specified as the first
     * command-line argument; read an integer k specified as the
     * second command line argument; then repeatedly processes
     * user queries, printing all occurrences of the given query
     * string in the text string with k characters of surrounding
     * context on either side.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length < 2) {
            System.err.println("Usage: java KWIK file.docx context");
            return;
        }

        String filePath = args[0];
        int context;
        try {
            context = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Second argument must be an integer: " + args[1]);
            return;
        }

        // Read text from .docx file
        StringBuilder textBuilder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XWPFDocument document = new XWPFDocument(fis)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            for (XWPFParagraph para : paragraphs) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    textBuilder.append(text).append(" ");
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading .docx file: " + e.getMessage());
            return;
        }

        // Standardize text (replace multiple spaces with single space)
        String text = textBuilder.toString().replaceAll("\\s+", " ");
        int n = text.length();

        // Build suffix array with Vietnamese alphabet
        SuffixArrayXVN sa = new SuffixArrayXVN(text);

        // Find all occurrences of queries and give context
        while (StdIn.hasNextLine()) {
            String query = StdIn.readLine();
            for (int i = sa.rank(query); i < n; i++) {
                int from1 = sa.index(i);
                int to1 = Math.min(n, from1 + query.length());
                if (!query.equals(text.substring(from1, to1))) break;
                int from2 = Math.max(0, sa.index(i) - context);
                int to2 = Math.min(n, sa.index(i) + context + query.length());
                StdOut.println(text.substring(from2, to2));
            }
            StdOut.println();
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2025, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
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
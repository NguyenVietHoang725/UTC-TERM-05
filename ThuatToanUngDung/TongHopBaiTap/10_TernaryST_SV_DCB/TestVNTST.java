/******************************************************************************
 *  Compilation:  javac TestVNTST.java
 *  Execution:    java TestVNTST input.docx
 *  Dependencies: VietnameseTST.java TST.java VietnameseAlphabet.java Alphabet.java Queue.java StdOut.java org.apache.poi.xwpf.usermodel.XWPFDocument
 *
 *  Test client for VietnameseTST class. Reads a .docx file containing student names
 *  and grades, stores them in a VietnameseTST, and performs operations like listing all students,
 *  searching by name, prefix, longest prefix, and pattern matching, with support for Vietnamese characters.
 *
 ******************************************************************************/

import java.io.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.util.ArrayList;
import java.util.List;

/**
 *  The {@code TestVNTST} class provides a test client for the {@link VietnameseTST}
 *  class. It reads a .docx file specified as a command-line argument, extracts student names
 *  and grades, and performs operations like listing all students, searching by name, prefix,
 *  longest prefix, and pattern matching, with support for Vietnamese text processing.
 *  <p>
 *  The input .docx file should contain lines in the format: "Name Grade" (e.g., "Nguyễn Việt Hoàng 9.8").
 *  <p>
 *  For additional documentation, see <a href="https://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author [Your Name]
 *  @modified for .docx file input and Vietnamese support
 */
public class TestVNTST {

    // Do not instantiate.
    private TestVNTST() { }

    /**
     * Reads a .docx file from command-line argument, extracts student names and grades,
     * and performs operations using VietnameseTST.
     *
     * @param args the command-line arguments (input.docx)
     */
    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length != 1) {
            System.err.println("Usage: java TestVNTST input.docx");
            return;
        }

        String filePath = args[0];
        VietnameseTST<Double> studentTST = new VietnameseTST<Double>();
        List<String> names = new ArrayList<>();
        List<Double> grades = new ArrayList<>();

        // Read text from .docx file
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    // Split each line into name and grade
                    String[] parts = text.split("\\s+(?=[0-9])"); // Split at space before a number
                    if (parts.length == 2) {
                        String name = parts[0].trim();
                        try {
                            Double grade = Double.parseDouble(parts[1].trim());
                            names.add(name);
                            grades.add(grade);
                            studentTST.put(name, grade);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid grade format for line: " + text);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid character in name: " + name);
                        }
                    } else {
                        System.err.println("Invalid line format: " + text);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return;
        }

        // In danh sách sinh viên theo thứ tự bảng chữ cái tiếng Việt
        System.out.println("Danh sách sinh viên theo thứ tự bảng chữ cái tiếng Việt:");
        for (String name : studentTST.keys()) {
            System.out.println(name + ": " + studentTST.get(name));
        }

        // Tìm theo tên (chọn tên đầu tiên nếu có)
        if (!names.isEmpty()) {
            String searchName = names.get(0);
            System.out.println("\nĐiểm của " + searchName + ": " + studentTST.get(searchName));
        }

        // Tìm theo tiền tố
        System.out.println("\nSinh viên có tiền tố 'Nguyễn':");
        for (String name : studentTST.keysWithPrefix("Nguyễn")) {
            System.out.println(name + ": " + studentTST.get(name));
        }

        // Tìm tiền tố dài nhất
        if (!names.isEmpty()) {
            String query = names.get(0) + " Test";
            System.out.println("\nTiền tố dài nhất của '" + query + "': " + studentTST.longestPrefixOf(query));
        }

        // Tìm keys khớp mẫu
        System.out.println("\nKeys khớp '.guyễn ...........':");
        for (String name : studentTST.keysThatMatch(".guyễn ...........")) {
            System.out.println(name + ": " + studentTST.get(name));
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
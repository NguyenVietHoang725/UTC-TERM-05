/******************************************************************************
 *  Compilation:  javac TestStudentTST.java
 *  Execution:    java TestStudentTST input.docx
 *  Dependencies: StudentTST.java VietnameseTST.java TST.java VietnameseAlphabet.java Alphabet.java Queue.java StdOut.java org.apache.poi.xwpf.usermodel.XWPFDocument
 *
 *  Test client for StudentTST class. Reads a .docx file containing student IDs,
 *  subjects, and grades, stores them in a StudentTST, and performs comprehensive tests
 *  including listing all students, retrieving grades, prefix search, longest prefix search,
 *  and pattern matching, with support for Vietnamese characters.
 *
 ******************************************************************************/

import java.io.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import java.util.ArrayList;
import java.util.List;

/**
 * Test client for StudentTST class. Reads a .docx file containing student IDs,
 * subjects, and grades, stores them in a StudentTST, and performs operations like
 * listing all students, retrieving grades, prefix search, longest prefix search,
 * and pattern matching, with support for Vietnamese text processing.
 */
public class TestStudentTST {
    private TestStudentTST() { }

    public static void main(String[] args) {
        // Validate command-line arguments
        if (args.length != 1) {
            System.err.println("Usage: java TestStudentTST input.docx");
            return;
        }

        String filePath = args[0];
        StudentTST studentTST = new StudentTST();
        List<String> studentIds = new ArrayList<>();

        // Read text from .docx file
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XWPFDocument document = new XWPFDocument(fis)) {
            boolean hasValidData = false;
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    // Split line into student ID, subject, and grade
                    String[] parts = text.split("\\s+");
                    if (parts.length == 3) {
                        String studentId = parts[0].trim();
                        String subject = parts[1].trim();
                        try {
                            Double grade = Double.parseDouble(parts[2].trim());
                            if (grade < 0 || grade > 10) {
                                System.err.println("Grade out of range [0, 10] for line: " + text);
                                continue;
                            }
                            studentTST.putStudent(studentId, subject, grade);
                            if (!studentIds.contains(studentId)) {
                                studentIds.add(studentId);
                            }
                            hasValidData = true;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid grade format for line: " + text);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Invalid character in line: " + text);
                        }
                    } else {
                        System.err.println("Invalid line format (expected: studentId subject grade): " + text);
                    }
                }
            }
            if (!hasValidData) {
                System.err.println("No valid data found in file: " + filePath);
                return;
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + filePath + ": " + e.getMessage());
            return;
        }

        // Test 1: List all students and their grades
        System.out.println("\n=== Test 1: Danh sách sinh viên và bảng điểm ===");
        if (studentTST.size() == 0) {
            System.out.println("Không có sinh viên nào trong danh sách.");
        } else {
            for (String id : studentTST.getAllStudents()) {
                System.out.println("Sinh viên: " + id);
                Iterable<String> subjects = studentTST.getGrades(id);
                boolean hasGrades = false;
                for (String subject : subjects) {
                    System.out.printf("\t%-20s: %.1f\n", subject, studentTST.getGrade(id, subject));
                    hasGrades = true;
                }
                if (!hasGrades) {
                    System.out.println("\tKhông có điểm môn nào.");
                }
            }
        }

        // Test 2: Retrieve grade for a specific student and subject
        System.out.println("\n=== Test 2: Truy xuất điểm của sinh viên ===");
        if (!studentIds.isEmpty()) {
            String studentId = studentIds.get(0);
            String subject = "Toán_rời_rạc"; // Use subject from your data
            Double grade = studentTST.getGrade(studentId, subject);
            System.out.printf("Điểm môn %s của %s: %s\n", subject, studentId, 
                grade != null ? String.format("%.1f", grade) : "Không tìm thấy");
            
            // Test with non-existent subject
            String nonExistentSubject = "Vật_lý";
            grade = studentTST.getGrade(studentId, nonExistentSubject);
            System.out.printf("Điểm môn %s của %s: %s\n", nonExistentSubject, studentId, 
                grade != null ? String.format("%.1f", grade) : "Không tìm thấy");
            
            // Test with non-existent student
            String nonExistentStudent = "999999999";
            grade = studentTST.getGrade(nonExistentStudent, subject);
            System.out.printf("Điểm môn %s của %s: %s\n", subject, nonExistentStudent, 
                grade != null ? String.format("%.1f", grade) : "Không tìm thấy");
        } else {
            System.out.println("Không có sinh viên nào để kiểm tra điểm.");
        }

        // Test 3: Find students by prefix
        System.out.println("\n=== Test 3: Tìm sinh viên theo tiền tố ===");
        String[] prefixes = {"231", "221", "88"}; // Test multiple prefixes
        for (String prefix : prefixes) {
            System.out.println("Sinh viên có mã bắt đầu bằng '" + prefix + "':");
            Iterable<String> students = studentTST.keysWithPrefix(prefix);
            boolean found = false;
            for (String id : students) {
                System.out.println("\t" + id);
                found = true;
            }
            if (!found) {
                System.out.println("\tKhông tìm thấy sinh viên nào.");
            }
        }

        // Test 4: Find longest prefix
        System.out.println("\n=== Test 4: Tìm tiền tố dài nhất ===");
        if (!studentIds.isEmpty()) {
            String studentId = studentIds.get(0);
            String query = studentId + "XYZ";
            System.out.println("Tiền tố dài nhất của '" + query + "': " + 
                (studentTST.longestPrefixOf(query) != null ? studentTST.longestPrefixOf(query) : "Không tìm thấy"));
            
            // Test with a partial prefix
            String partialQuery = studentId.substring(0, 3) + "ABC";
            System.out.println("Tiền tố dài nhất của '" + partialQuery + "': " + 
                (studentTST.longestPrefixOf(partialQuery) != null ? studentTST.longestPrefixOf(partialQuery) : "Không tìm thấy"));
        } else {
            System.out.println("Không có sinh viên nào để kiểm tra tiền tố dài nhất.");
        }

        // Test 5: Find keys that match a pattern
        System.out.println("\n=== Test 5: Tìm mã sinh viên khớp mẫu ===");
        String pattern = "231230..."; // Pattern for 9-digit IDs starting with "231230"
        System.out.println("Mã sinh viên khớp mẫu '" + pattern + "':");
        Iterable<String> matches = studentTST.keysThatMatch(pattern);
        boolean found = false;
        for (String id : matches) {
            System.out.println("\t" + id);
            found = true;
        }
        if (!found) {
            System.out.println("\tKhông tìm thấy mã sinh viên nào khớp mẫu.");
        }
    }
}

/******************************************************************************
 *  Copyright 2002-2025, Robert Sedgewick and Kevin Wayne.
 *  Modified for .docx file input and Vietnamese support.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *      Algorithms, 4th Edition by Robert Sedgewick and Kevin Wayne,
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
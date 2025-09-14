/******************************************************************************
 *  Compilation:  javac Quick3stringVietnamese.java
 *  Execution:    java Quick3stringVietnamese < input.txt
 *  Dependencies: StdIn.java StdOut.java StdRandom.java Alphabet.java VietnameseAlphabet.java Quick3string.java
 *
 *  Sorts an array of strings using 3-way radix quicksort with Vietnamese alphabet.
 *
 ******************************************************************************/

public class Quick3stringVietnamese extends Quick3string {

    // Constructor to initialize with Vietnamese alphabet
    public Quick3stringVietnamese() {
        super(VietnameseAlphabet.VIETNAMESE_ALPHABET);
    }

    // Override charAt to use VietnameseAlphabet indices
    @Override
    protected int charAt(String s, int d) {
        if (d >= s.length()) return -1;
        char c = s.charAt(d);
        return alphabet.contains(c) ? alphabet.toIndex(c) : -1;
    }

    // Override less to compare using VietnameseAlphabet indices
    @Override
    protected boolean less(String v, String w, int d) {
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            int vIndex = charAt(v, i);
            int wIndex = charAt(w, i);
            if (vIndex < wIndex) return true;
            if (vIndex > wIndex) return false;
        }
        return v.length() < w.length();
    }

    /**
     * Reads in a sequence of strings from standard input;
     * 3-way radix quicksorts them using Vietnamese alphabet;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Quick3stringVietnamese sorter = new Quick3stringVietnamese();
        sorter.sort(a);
        for (String s : a)
            StdOut.println(s);
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
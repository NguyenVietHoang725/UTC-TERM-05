/******************************************************************************
 *  Compilation:  javac Quick3string.java
 *  Execution:    java Quick3string < input.txt
 *  Dependencies: StdIn.java StdOut.java StdRandom.java Alphabet.java
 *
 *  Sorts an array of strings using 3-way radix quicksort with a specified alphabet.
 *
 ******************************************************************************/

public class Quick3string {
    protected static final int CUTOFF = 15; // cutoff to insertion sort
    protected final Alphabet alphabet;      // the alphabet to use for sorting

    // Constructor to initialize with a specific alphabet
    public Quick3string(Alphabet alphabet) {
        this.alphabet = alphabet;
    }

    /**
     * Rearranges the array of strings in ascending order using the provided alphabet.
     *
     * @param a the array to be sorted
     */
    public void sort(String[] a) {
        StdRandom.shuffle(a);
        sort(a, 0, a.length - 1, 0);
        assert isSorted(a);
    }

    // return the dth character of s, -1 if d = length of s
    protected int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d >= s.length()) return -1;
        return s.charAt(d); // Default to Unicode value
    }

    // 3-way string quicksort a[lo..hi] starting at dth character
    protected void sort(String[] a, int lo, int hi, int d) {
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else i++;
        }

        sort(a, lo, lt - 1, d);
        if (v >= 0) sort(a, lt, gt, d + 1);
        sort(a, gt + 1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    protected void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--)
                exch(a, j, j - 1);
    }

    // exchange a[i] and a[j]
    protected void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    protected boolean less(String v, String w, int d) {
        assert v.substring(0, d).equals(w.substring(0, d));
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (charAt(v, i) < charAt(w, i)) return true;
            if (charAt(v, i) > charAt(w, i)) return false;
        }
        return v.length() < w.length();
    }

    // is the array sorted
    protected boolean isSorted(String[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i - 1], 0)) return false;
        return true;
    }

    /**
     * Reads in a sequence of strings from standard input;
     * 3-way radix quicksorts them using the default ASCII alphabet;
     * and prints them to standard output in ascending order.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        String[] a = StdIn.readAllStrings();
        Quick3string sorter = new Quick3string(Alphabet.ASCII);
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
 import java.io.*;
/**
 * Write a description of class TestLSD here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TestLSD
{
    public static void main(String[] args) throws IOException { 
        System.setIn(new FileInputStream(new File("masinhvien.txt")));
        String[] a = StdIn.readAllStrings();
        int n = a.length;

        // check that strings have fixed length
        //int w = a[0].length();
        int w = 9;
        //for (int i = 0; i < n; i++)
        //    assert a[i].length() == w : "Strings must have fixed length";

        // sort the strings
        LSD.sort(a, w);

        // print results
        for (int i = 0; i < n; i++)
            StdOut.println(a[i]);
    }
}

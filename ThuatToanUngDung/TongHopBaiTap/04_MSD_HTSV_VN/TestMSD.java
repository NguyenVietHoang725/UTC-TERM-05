 import java.io.*;
 import java.util.*;
/**
 * Write a description of class TestMSD here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class TestMSD
{
    public static void main(String[] args) throws IOException { 
        System.setIn(new FileInputStream(new File("hotensinhvien.txt")));
        
        ArrayList<String> lines = new ArrayList<>();
        
        while (StdIn.hasNextLine()) {
            String line = StdIn.readLine();
            if (!line.trim().isEmpty()) {
                lines.add(line.trim());
            }
        }
        
        String[] a = lines.toArray(new String[0]);
        int n = a.length;
        
        MSD.sort(a);
        
        for (int i = 0; i < n; i++)
            StdOut.println(a[i]);
    }
}

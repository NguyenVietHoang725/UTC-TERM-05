
/**
 * Test Alphabet UNICODE16 (65536) by using a short Vietnamese text.
 *
 * @author: Nguyen Viet Hoang
 * @version: 17/08/2025
 */
public class TestAlphabet
{
    public static void main(String[] args) {
        
        String vnText = "Xin chào! Tôi tên là Nguyễn Việt Hoàng. Rất vui khi được gặp bạn";
        
        // Alphabet UNICODE16
        Alphabet unicode16 = Alphabet.UNICODE16;
        
        try {
            // Convert String to Char Array 
            int[] indices = unicode16.toIndices(vnText);
            
            // Convert Char Array to String
            String decodedText = unicode16.toChars(indices);
            
            StdOut.println("Vietnamese Text: " + vnText);
            
            StdOut.println("UNICODE16 Text: " + decodedText);
        } catch (IllegalArgumentException e) {
            StdOut.println("Error: " + e.getMessage());
        }
    }
}

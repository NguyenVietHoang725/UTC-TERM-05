/**
 * Lớp {@code GREPVN} cung cấp một client để đọc các dòng văn bản từ một file
 * và in ra đầu ra chuẩn các dòng chứa chuỗi con khớp với regular expression
 * được chỉ định, sử dụng bảng chữ cái tiếng Việt thông qua {@code NFAVN}.
 * <p>
 * Lớp này kế thừa từ {@code GREP} và sử dụng {@code NFAVN} để hỗ trợ các ký tự
 * tiếng Việt (bao gồm chữ cái có dấu, dấu thanh rời, ký hiệu, v.v.).
 * Regular expression được bao bọc bởi "(.*" và ".*)" để khớp với chuỗi con.
 * <p>
 * Để biết thêm chi tiết, xem <a href="https://algs4.cs.princeton.edu/31elementary">Section 3.1</a> của
 * <i>Algorithms, 4th Edition</i> bởi Robert Sedgewick và Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 * @author [Your Name]
 */
public class GREPVN extends GREP {

    // Không cho phép khởi tạo
    private GREPVN() { }

    /**
     * Đọc regular expression từ đối số dòng lệnh (hỗ trợ closure, binary or, parentheses),
     * đọc các dòng từ file được chỉ định, và in ra đầu ra chuẩn các dòng chứa chuỗi con khớp với
     * regular expression, sử dụng bảng chữ cái tiếng Việt.
     *
     * @param args đối số dòng lệnh: args[0] là regular expression, args[1] là tên file đầu vào
     * @throws IllegalArgumentException nếu regular expression hoặc file đầu vào không hợp lệ
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            StdOut.println("Usage: java GREPVN <regexp> <filename>");
            return;
        }

        String regexp = "(.*" + args[0] + ".*)";
        NFAVN nfa = new NFAVN(regexp);

        // Đọc file từng dòng
        In in = new In(args[1]);
        while (in.hasNextLine()) {
            String line = in.readLine();
            try {
                if (nfa.recognizes(line)) {
                    StdOut.println(line);
                }
            } catch (IllegalArgumentException e) {
                // Bỏ qua các dòng chứa ký tự không hợp lệ
                continue;
            }
        }
    }
}
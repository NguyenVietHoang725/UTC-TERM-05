/**
 * Lớp {@code MSD} cung cấp các phương thức tĩnh để sắp xếp một mảng chuỗi
 * sử dụng thuật toán MSD radix sort, được điều chỉnh cho bảng chữ cái tiếng Việt.
 */
public class MSD {
    private static final int R = VietnameseAlphabet.VIETNAMESE_ALPHABET.radix(); // Số lượng ký tự trong bảng chữ cái
    private static final int CUTOFF = 15; // Ngưỡng chuyển sang insertion sort

    // Không khởi tạo đối tượng
    private MSD() {}

    /**
     * Sắp xếp mảng chuỗi theo thứ tự tăng dần sử dụng MSD radix sort.
     *
     * @param a mảng chuỗi cần sắp xếp
     */
    public static void sort(String[] a) {
        int n = a.length;
        String[] aux = new String[n];
        sort(a, 0, n - 1, 0, aux);
    }

    // Trả về chỉ số của ký tự tại vị trí d trong chuỗi s, -1 nếu d vượt quá độ dài chuỗi
    private static int charAt(String s, int d) {
        if (d >= s.length()) return -1;
        char c = s.charAt(d);
        return VietnameseAlphabet.VIETNAMESE_ALPHABET.contains(c) ? VietnameseAlphabet.VIETNAMESE_ALPHABET.toIndex(c) : R;
    }

    // Sắp xếp từ a[lo] đến a[hi], bắt đầu từ ký tự thứ d
    private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
        // Chuyển sang insertion sort cho mảng con nhỏ
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        // Đếm tần suất
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            count[c + 2]++;
        }

        // Chuyển đổi tần suất thành chỉ số
        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }

        // Phân phối
        for (int i = lo; i <= hi; i++) {
            int c = charAt(a[i], d);
            aux[count[c + 1]++] = a[i];
        }

        // Sao chép lại
        for (int i = lo; i <= hi; i++) {
            a[i] = aux[i - lo];
        }

        // Đệ quy sắp xếp cho từng ký tự
        for (int r = 0; r < R; r++) {
            sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
        }
    }

    // Insertion sort từ a[lo..hi], bắt đầu từ ký tự thứ d
    private static void insertion(String[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1], d); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    // So sánh hai chuỗi v và w, bắt đầu từ ký tự thứ d
    private static boolean less(String v, String w, int d) {
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            char vc = v.charAt(i);
            char wc = w.charAt(i);
            int orderV = VietnameseAlphabet.VIETNAMESE_ALPHABET.contains(vc) ? VietnameseAlphabet.VIETNAMESE_ALPHABET.toIndex(vc) : R;
            int orderW = VietnameseAlphabet.VIETNAMESE_ALPHABET.contains(wc) ? VietnameseAlphabet.VIETNAMESE_ALPHABET.toIndex(wc) : R;
            if (orderV < orderW) return true;
            if (orderV > orderW) return false;
        }
        return v.length() < w.length();
    }

    // Hoán đổi a[i] và a[j]
    private static void exch(String[] a, int i, int j) {
        String temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    /**
     * Đọc chuỗi từ đầu vào, sắp xếp bằng MSD radix sort, và in ra theo thứ tự tăng dần.
     */
    public static void main(String[] args) {
        String[] a = {"Nguyễn Văn An", "Lê Thị Bình", "Phạm Văn Cường", "Nguyễn Thị Ánh"};
        sort(a);
        for (String s : a) {
            System.out.println(s);
        }
    }
}
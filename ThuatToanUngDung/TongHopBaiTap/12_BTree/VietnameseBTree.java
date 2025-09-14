import java.util.Random;

/**
 * Lớp {@code VietnameseBTree} kế thừa từ {@code BTree} để lưu trữ danh sách các cặp tên và điểm trung bình.
 * Sử dụng để quản lý danh sách 17 bạn với tên tiếng Việt và điểm ngẫu nhiên từ 0.0 đến 10.0.
 * Kế thừa tất cả các phương thức từ {@code BTree} và chỉ thêm logic để khởi tạo dữ liệu.
 */
public class VietnameseBTree extends BTree<String, Double> {
    private static final String[] NAMES = {
        "An", "Anh", "Ánh", "Ba", "Bình", "Bính", "Lan", "Lân", "Lanh",
        "Quang", "Quảng", "Quỳnh", "Quân", "Thai", "Thành", "Thắng", "Thông"
    };
    private static final Random RANDOM = new Random();

    /**
     * Khởi tạo một B-Tree và chèn 17 cặp tên-điểm.
     */
    public VietnameseBTree() {
        super();
        initialize();
    }

    /**
     * Khởi tạo danh sách 17 bạn với tên và điểm ngẫu nhiên.
     */
    private void initialize() {
        for (String name : NAMES) {
            // Tạo điểm ngẫu nhiên từ 0.0 đến 10.0, làm tròn đến 2 chữ số thập phân
            double score = Math.round(RANDOM.nextDouble() * 10.0 * 100.0) / 100.0;
            put(name, score);
        }
    }

    /**
     * Đơn vị kiểm tra cho {@code VietnameseBTree}.
     */
    public static void main(String[] args) {
        VietnameseBTree tree = new VietnameseBTree();

        // In ra một số tên và điểm để kiểm tra
        System.out.println("An:        " + tree.get("An"));
        System.out.println("Bình:      " + tree.get("Bình"));
        System.out.println("Quỳnh:     " + tree.get("Quỳnh"));
        System.out.println("Thành:     " + tree.get("Thành"));
        System.out.println("KhôngTồnTại: " + tree.get("KhôngTồnTại"));
        System.out.println();

        // In thông tin cây
        System.out.println("Kích thước: " + tree.size());
        System.out.println("Chiều cao:  " + tree.height());
        System.out.println(tree);
    }
}
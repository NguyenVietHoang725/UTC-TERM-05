import vn.pipeline.VnCoreNLP;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * Class chính để chạy hệ thống hỗ trợ soạn thảo với autocomplete, đọc văn bản từ file .docx.
 */
public class AutocompleteSystem {
    public static void main(String[] args) throws Exception {
        // Khởi tạo VnCoreNLP
        String[] annotators = {"wseg"};
        VnCoreNLP pipeline = new VnCoreNLP(annotators);

        // Đọc văn bản từ file .docx
        String filePath = args.length > 0 ? args[0] : "vanban.docx"; // Lấy từ args hoặc hardcode
        StringBuilder textBuilder = new StringBuilder();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText().trim();
                if (!text.isEmpty()) {
                    textBuilder.append(text).append(" ");
                }
            }
        } catch (Exception e) {
            StdOut.println("Lỗi khi đọc file " + filePath + ": " + e.getMessage());
            return;
        }

        // Chuẩn hóa văn bản (loại bỏ khoảng trắng thừa)
        String inputText = textBuilder.toString().trim().replaceAll("\\s+", " ");
        StdOut.println("Văn bản đọc được: " + inputText);

        // Tách từ bằng TextProcessor
        List<String> words = TextProcessor.segmentText(inputText);

        // In danh sách từ đã tách để kiểm tra
        StdOut.println("Danh sách từ đã tách:");
        for (int i = 0; i < words.size(); i++) {
            StdOut.println((i + 1) + ". " + words.get(i));
        }

        // Xây dựng Trie với VietnameseAlphabet
        TrieST<Integer> trie = new TrieST<>(VietnameseAlphabet.VIETNAMESE_ALPHABET);
        for (int i = 0; i < words.size(); i++) {
            trie.put(words.get(i), i);
        }

        // Vòng lặp autocomplete
        Scanner scanner = new Scanner(System.in, "UTF-8");
        int nextPosition = words.size();
        while (true) {
            StdOut.println("Nhập prefix (kết thúc bằng dấu cách để gợi ý, hoặc cụm mới kết thúc bằng / để thêm):");
            String input = scanner.nextLine();
            if (input.isEmpty()) break;

            if (input.endsWith(" ")) {
                // Gợi ý với keysWithPrefix
                String prefix = input.trim();
                Iterable<String> suggestions = trie.keysWithPrefix(prefix);
                int index = 1;
                StdOut.println("Gợi ý cho prefix '" + prefix + "':");
                boolean hasSuggestions = false;
                for (String suggestion : suggestions) {
                    StdOut.println(index + ". " + suggestion + " (vị trí: " + trie.get(suggestion) + ")");
                    index++;
                    hasSuggestions = true;
                }
                if (!hasSuggestions) {
                    StdOut.println("Không có gợi ý nào. Hãy nhập cụm mới kết thúc bằng / để thêm.");
                }
            } else if (input.endsWith("/")) {
                // Thêm cụm mới
                String newWord = input.substring(0, input.length() - 1).trim();
                trie.put(newWord, nextPosition);
                StdOut.println("Đã thêm cụm mới: " + newWord + " (vị trí: " + nextPosition + ")");
                nextPosition++;
            } else {
                StdOut.println("Lỗi nhập: Kết thúc bằng dấu cách cho gợi ý hoặc / cho thêm mới.");
            }
        }
    }
}
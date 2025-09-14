import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Test {
    public static void main(String[] args) {
        // Đọc toàn bộ file program.c
        String text;
        try {
            text = Files.readString(Path.of("program.c"));
        } catch (IOException e) {
            System.out.println("Lỗi khi đọc file: " + e.getMessage());
            return;
        }

        // Chuẩn hóa chuỗi (gom khoảng trắng)
        String cleanedText = text.replaceAll("\\s+", " ");

        // Tìm xâu con lặp dài nhất
        String result = LongestRepeatedSubstring.lrs(cleanedText);

        System.out.println("Xâu con lặp dài nhất: '" + result + "'");

        // Kiểm tra xem có giống hàm không
        if (isLikelyFunction(result)) {
            System.out.println("\nGợi ý: Đoạn lặp này có thể là một hàm.");
            suggestFunctionExtraction(result);
        } else {
            System.out.println("\nKhông nhận diện được như một hàm C.");
        }
    }

    // Kiểm tra xem chuỗi có giống hàm C hay không
    private static boolean isLikelyFunction(String substring) {
        return substring.contains("{") && substring.contains("}")
                || substring.matches(".*\\w+\\s*\\([^)]*\\).*");
    }

    // Đưa ra gợi ý tách hàm
    private static void suggestFunctionExtraction(String substring) {
        String functionName = "extractedFunction";
        String returnType = "void";
        String parameters = "";

        if (substring.matches("\\w+\\s+\\w+\\s*\\([^)]*\\).*")) {
            String[] parts = substring.trim().split("\\s+", 2);
            if (parts.length > 1) {
                returnType = parts[0]; 
                String nameAndParams = parts[1].split("\\{")[0].trim();
                functionName = nameAndParams.split("\\(")[0].trim();
                parameters = nameAndParams.substring(nameAndParams.indexOf("("));
            }
        }

        System.out.println("Hàm C được đề xuất:");
        System.out.println(returnType + " " + functionName + parameters + " {");
        System.out.println("    // ... thân hàm ở đây ...");
        System.out.println("}");
    }
}

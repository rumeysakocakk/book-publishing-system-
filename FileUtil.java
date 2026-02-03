package midproject1;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static void writeToFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
            System.out.println("Dosya başarıyla yazıldı: " + path);
        } catch (IOException e) {
            System.err.println("Dosyaya yazılamadı: " + e.getMessage());
        }
    }
}

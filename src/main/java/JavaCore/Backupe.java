package JavaCore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Backupe {

    public Backupe() throws IOException {
        File sourceDir = new File("src/main/java/JavaCore"); // Укажите путь к исходной директории
        File backupDir = new File("./backup");
        backupDirectory(sourceDir, backupDir);
    }

    public  void backupDirectory(File sourceDir, File backupDir) throws IOException {
        if (!backupDir.exists()) {
            backupDir.mkdir();
        }

        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    File backupFile = new File(backupDir, file.getName());
                    Files.copy(file.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}

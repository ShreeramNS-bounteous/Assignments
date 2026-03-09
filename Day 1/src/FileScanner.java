import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileScanner {
    public static List<File> getFiles() {

        File folder = new File("files");

        File[] files = folder.listFiles();

        List<File> selectedFiles = new ArrayList<>();

        if (files == null) {
            System.out.println("No files found or directory does not exist.");
            return selectedFiles;
        }

        Arrays.sort(files, (f1, f2) ->
                Long.compare(f1.lastModified(), f2.lastModified()));

        for (File file : files) {

            if (file != null && file.isFile()) {
                selectedFiles.add(file);
            }
        }

        return selectedFiles;
    }
}

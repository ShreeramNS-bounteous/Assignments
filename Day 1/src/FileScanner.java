import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileScanner {
    public  static List<File > getFiles(){
        File folder = new File("files");

        File[] files = folder.listFiles();

        if(files == null){
            return new ArrayList<>();
        }

        Arrays.sort(files,(f1, f2)->Long.compare(f1.lastModified(),f2.lastModified()));

        List<File> selectedFiles = new ArrayList<>();

        for (int i = 0; i < Math.min(100,files.length); i++) {
            if(files[i].isFile()){
                selectedFiles.add(files[i]);
            }
        }

        return selectedFiles;

    }
}

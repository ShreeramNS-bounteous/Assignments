import java.io.File;
import java.util.Arrays;
import java.util.List;

class Main{
    static void main() {
//        FileGenerator.generateFiles();

        List<File> files = FileScanner.getFiles();

        for (File file: files){
            System.out.println(file.getName());
        }
    }
}
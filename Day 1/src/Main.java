import java.io.File;
import java.util.Arrays;
import java.util.List;

class Main{
    static void main() {
//        FileGenerator.generateFiles();

//        List<File> files = FileScanner.getFiles();
//
//        for (File file: files){
//            System.out.println(file.getName());
//        }

       File file = new File("files/file1.txt");
       FileResult result = FileProcessor.processFile(file);

        System.out.println("File: " + result.getFileName());
        System.out.println("Line Count: " + result.getLineCount());
        System.out.println("Word Count: " + result.getWordCount());
    }
}
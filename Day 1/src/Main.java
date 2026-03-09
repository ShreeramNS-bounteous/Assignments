import java.io.File;
import java.util.Arrays;
import java.util.List;

class Main{
    static void main() {
        System.out.println("Processing files using SINGLE thread...\n");
        long startTime = System.currentTimeMillis();

        List<File> files = FileScanner.getFiles();

        int totalLines  = 0;
        int totalWords = 0;

        for (File file : files){
            FileResult result = FileProcessor.processFile(file);

            System.out.println("File: " + result.getFileName());
            System.out.println("Line Count: " + result.getLineCount());
            System.out.println("Word Count: " + result.getWordCount());

            totalLines+= result.getLineCount();
            totalWords+= result.getWordCount();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("----------------------------------");
        System.out.println("Summary");
        System.out.println("----------------------------------");

        System.out.println("Total Files Processed: " + files.size());
        System.out.println("Total Lines: " + totalLines);
        System.out.println("Total Words: " + totalWords);

        System.out.println("Execution Time: " + (endTime - startTime) + " ms");
    }
}
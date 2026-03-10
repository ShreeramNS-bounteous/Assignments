import java.io.File;
import java.util.List;

class SingleThreadedFileProcessing {

    static void main() {

        System.out.println("Processing files using SINGLE thread...\n");

        long startTime = System.nanoTime();

        List<File> files = FileScanner.getFiles();

        int totalLines = 0;
        int totalWords = 0;

        int batchSize = 100;

        for (int start = 0; start < files.size(); start += batchSize) {

            int end = Math.min(start + batchSize, files.size());

            List<File> batch = files.subList(start, end);

            System.out.println("Processing Batch: " + (start + 1) + " to " + end);

            for (File file : batch) {

                FileResult result = FileProcessor.processFile(file);

                System.out.println("File: " + result.getFileName());
                System.out.println("Lines: " + result.getLineCount());
                System.out.println("Words: " + result.getWordCount());
                System.out.println();

                totalLines += result.getLineCount();
                totalWords += result.getWordCount();
            }

            System.out.println("Batch completed\n");
        }

        long endTime = System.nanoTime();

        System.out.println("----------------------------------");
        System.out.println("Summary");
        System.out.println("----------------------------------");

        System.out.println("Total Files Processed: " + files.size());
        System.out.println("Total Lines: " + totalLines);
        System.out.println("Total Words: " + totalWords);

        System.out.println("Execution Time: " + (endTime - startTime) / 1_000_000 + " ms");
    }
}
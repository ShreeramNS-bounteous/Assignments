import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Test {

    static void main() {

        System.out.println("Processing files using 4 threads...\n");

        long startTime = System.nanoTime();

        List<File> files = FileScanner.getFiles();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        int totalLines = 0;
        int totalWords = 0;

        int batchSize = 100;

        for (int start = 0; start < files.size(); start += batchSize) {

            int end = Math.min(start + batchSize, files.size());

            List<File> batch = files.subList(start, end);

            System.out.println("Processing Batch: " + (start + 1) + " to " + end);

            List<Future<FileResult>> futures = new ArrayList<>();

            for (File file : batch) {

                FileTask task = new FileTask(file);

                Future<FileResult> future = executor.submit(task);

                futures.add(future);
            }

            for (Future<FileResult> future : futures) {

                try {

                    FileResult result = future.get();

                    System.out.println("File: " + result.getFileName());
                    System.out.println("Lines: " + result.getLineCount());
                    System.out.println("Words: " + result.getWordCount());
                    System.out.println();

                    totalLines += result.getLineCount();
                    totalWords += result.getWordCount();

                } catch (Exception e) {

                    System.out.println("Error retrieving result");

                }
            }

            System.out.println("Batch completed\n");
        }

        executor.shutdown();


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
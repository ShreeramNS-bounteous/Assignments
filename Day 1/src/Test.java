import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Test {
    static void main() {
        System.out.println("Processing files using 4 threads...\n");
        long startTime = System.nanoTime();

        //get top 100 files
        List<File> files = FileScanner.getFiles();

        //create threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        //list to store future result of each task
        List<Future<FileResult>>  futures = new ArrayList<>();

        //create task and execute it and store future result in futures list
        for(File file :files){
            FileTask task = new FileTask(file);
            Future<FileResult> future = executor.submit(task);
            futures.add(future);
        }

        int totalLines = 0;
        int totalWords = 0;

        for(Future<FileResult> future : futures){
            try{
                // Main thread waits for workers
                FileResult result = future.get();
                System.out.println("File: " + result.getFileName());
                System.out.println("Lines: " + result.getLineCount());
                System.out.println("Words: " + result.getWordCount());
                System.out.println();

                totalLines += result.getLineCount();
                totalWords += result.getWordCount();
            }catch (Exception e) {

                System.out.println("Error retrieving result");

            }
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

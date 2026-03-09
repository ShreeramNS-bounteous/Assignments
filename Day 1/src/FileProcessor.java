import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class FileProcessor {
    public static FileResult processFile(File file){
        int lineCount = 0;
        int wordCount = 0;
       try{
           BufferedReader reader = new BufferedReader(new FileReader(file));


           String line;

           while ((line = reader.readLine()) != null){
               lineCount++;
               String[] words = line.trim().split("\\s+");
               wordCount += words.length;
           }

           reader.close();

       } catch (Exception e) {
           throw new RuntimeException(e);
       }

       return new FileResult(
               file.getName(),
               lineCount,
               wordCount
       );
    }
}

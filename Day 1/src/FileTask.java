import java.io.File;
import java.util.concurrent.Callable;

public class FileTask implements Callable<FileResult> {

    private File file;

    public FileTask(File file){
        this.file = file;
    }

    @Override
    public  FileResult call(){
        try{
            return FileProcessor.processFile(file);
        }catch (Exception e){
            System.out.println("Error processing file: "+ file.getName());
            return new FileResult(file.getName(), 0,0);
        }
    }
}

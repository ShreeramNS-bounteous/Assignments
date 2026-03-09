import java.io.File;

public class FileGenerator {

    public static void createDirectory() {

        File folder = new File("files");

        if (!folder.exists()) {
            boolean created = folder.mkdir();

            if (created) {
                System.out.println("Directory 'files' created successfully.");
            } else {
                System.out.println("Failed to create directory.");
            }

        } else {
            System.out.println("Directory already exists.");
        }
    }
}
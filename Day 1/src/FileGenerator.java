import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

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

    public static void generateFiles()  {

        File folder = new File("files");

        if (!folder.exists()) {
            folder.mkdir();
            System.out.println("Created files directory");
        }

        String[] sentences = {
                "Java is powerful",
                "Multithreading improves performance",
                "ExecutorService manages threads efficiently",
                "Concurrency allows tasks to run in parallel",
                "Spring Boot simplifies backend development",
                "Security is important in applications",
                "Streams make data processing easier",
                "Collections help manage groups of objects"
        };

        Random random = new Random();
        try {
        for (int i = 1; i <= 120; i++) {

            File file = new File("files/file" + i + ".txt");

            FileWriter writer = null;

                writer = new FileWriter(file);


            int numberOfLines = random.nextInt(3) + 3;

            for (int j = 0; j < numberOfLines; j++) {

                String line = sentences[random.nextInt(sentences.length)];

                writer.write(line + "\n");
            }

            writer.close();

            System.out.println("Created file" + i + ".txt");
        }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
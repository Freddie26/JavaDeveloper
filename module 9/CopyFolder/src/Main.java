import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Программа копирования папки...");

        Path sourceFolder = getFolder("Введите папку-источник:", true);
        Path targetFolder = getFolder("Введите папку назначения:", false);

        try {
            copyFiles(sourceFolder, targetFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Path getFolder(String message, boolean pathMustExist) {
        for(;;) {
            System.out.println(message);

            String line = scanner.nextLine();

            Path folderPath = Paths.get(line);
            if (!pathMustExist || Files.exists(folderPath)) {
                return folderPath;
            }
        }
    }

    private static void copyFiles(Path sourceFolder, Path targetFolder) throws IOException {
        Files.walk(sourceFolder, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .map(File::getAbsolutePath)
                .map(absoluteFileString -> absoluteFileString.replace(sourceFolder.toString(), ""))
                .forEach(shortFileString -> {
                    try {
                        Path sourceFilePath = Paths.get(sourceFolder + shortFileString);
                        Path targetFilePath = Paths.get(targetFolder + shortFileString);

                        if (Files.notExists(targetFilePath)) {
                            Files.createDirectories(targetFilePath);
                        }

                        Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}

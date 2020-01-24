import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        //noinspection InfiniteLoopStatement
        for (;;) {
            logger.info("Введите путь папки для получения информации о файлах в ней:");
            try {
                Path folderPath = Paths.get(scanner.nextLine());

                logger.info("Переданный путь: [{}]", folderPath);

                if (Files.isDirectory(folderPath)) {
                    long folderLength = Files.walk(folderPath, Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
                            .filter(p -> !Files.isDirectory(p))
                            .map(Path::toFile)
                            .mapToLong(File::length)
                            .sum();

                    logger.info("Размер папки [{}]", formatSize(folderLength));
                } else {
                    logger.warn("Переданный путь [{}] не является директорией!", folderPath);
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }

    public static String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z * 10)), " KMGTPE".charAt(z));
    }
}

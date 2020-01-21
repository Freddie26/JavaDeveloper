import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        logger.info("Введите путь папки для получения информации о файлах в ней:");

        try {
            //noinspection InfiniteLoopStatement
            for (;;) {
                String folderPath = scanner.nextLine();

                File initFolder = new File(folderPath);
                if (initFolder.isDirectory()) {
                    scanFolder(initFolder);
                }
                else {
                    logger.warn("Переданный путь [{}] не является директорией!", folderPath);
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private static void scanFolder(File folder) {
        File[] files = folder.listFiles();

        if (files == null) {
            logger.info("Папка {} не содержит файлов", folder.getPath());
            return;
        }

        for(File file: files) {
            if (file.isFile()) {
                logger.info("Файл [{}], расположенный в [{}] имеет размер - [{}] кб",
                        file.getName(), folder.getAbsolutePath(), file.length() / 1024);
            } else {
                scanFolder(file);
            }
        }
    }

}

package bg.sofia.uni.fmi.mjt.photoalbum;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // Пътят до директорията за изходни и входни изображения
        String sourceDirectory = "src/test/resources/sourceImages";
        String outputDirectory = "src/test/resources/outputImages";

        // Създаване на директориите, ако не съществуват
        createDirectory(sourceDirectory);
        createDirectory(outputDirectory);


        // Създаване на инстанция на ParallelMonochromeAlbumCreator с 4 потребителски нишки
        MonochromeAlbumCreator albumCreator = new ParallelMonochromeAlbumCreator(4);
        albumCreator.processImages(sourceDirectory, outputDirectory);
    }

    private static void createDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
}

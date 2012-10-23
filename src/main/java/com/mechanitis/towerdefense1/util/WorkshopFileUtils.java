package com.mechanitis.towerdefense1.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WorkshopFileUtils {

    public static void ensureDirectoryExists(final Path fileLocation) {
        if (!Files.exists(fileLocation)) {
            try {
                Files.createDirectory(fileLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

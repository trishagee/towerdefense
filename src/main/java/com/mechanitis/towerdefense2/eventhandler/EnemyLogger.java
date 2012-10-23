package com.mechanitis.towerdefense2.eventhandler;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense2.util.WorkshopFileUtils;
import com.mechanitis.towerdefense2.Enemy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class EnemyLogger implements EventHandler<Enemy> {

    private Path logFile;

    public EnemyLogger(final Path logFileDirectory) {
        try {
            WorkshopFileUtils.ensureDirectoryExists(logFileDirectory);

            logFile = logFileDirectory.resolve("towerdefense.log");
            Files.deleteIfExists(logFile);
            Files.createFile(logFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(String.valueOf(sequenceNumber));
            writer.write(",");
            enemy.describeTo(writer);
            writer.newLine();
        }
        if (endOfBatch) {
            // here you could do something around flushing batches
        }
    }

}

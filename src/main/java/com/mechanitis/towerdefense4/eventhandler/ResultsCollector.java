package com.mechanitis.towerdefense4.eventhandler;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ResultsCollector implements EventHandler<Enemy> {
    private static final String FORMAT = "%5d; Alive for %6d millis; Travelled %6d metres; Shot at:%s; Status: %s%n";

    private Path resultFile;

    public ResultsCollector(final Path logDirectory) {
        try {
            resultFile = logDirectory.resolve("resultsDifferentTurrets.log");
            Files.deleteIfExists(resultFile);
            Files.createFile(resultFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        try (BufferedWriter writer = Files.newBufferedWriter(resultFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            final String status = enemy.isDead() ? "DESTROYED" : "ALIVE AND WELL";
            writer.write(String.format(FORMAT,
                    sequenceNumber,
                    enemy.getAge(),
                    enemy.getTotalDistanceTravelledInMetres(),
                    enemy.wasShotAt(),
                    status));
        }
    }

}
package com.mechanitis.towerdefense1.eventhandler;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense1.Enemy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ResultsCollector implements EventHandler<Enemy> {
    private static final String FORMAT = "%5d; Alive for %6d millis; Travelled %6d metres.%n";

    private Path resultFile;
    private long ageOfOldestSurvivor = -1L;

    public ResultsCollector(final Path logDirectory) {
        try {
            resultFile = logDirectory.resolve("results.log");
            Files.deleteIfExists(resultFile);
            Files.createFile(resultFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        final long timeSurvived = enemy.getAge();
        if (timeSurvived > ageOfOldestSurvivor) {
            ageOfOldestSurvivor = timeSurvived;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(resultFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(String.format(FORMAT, sequenceNumber, timeSurvived, enemy.getTotalDistanceTravelledInMetres()));
        }
    }

    public long getAgeOfOldestSurvivor() {
        return ageOfOldestSurvivor;
    }
}

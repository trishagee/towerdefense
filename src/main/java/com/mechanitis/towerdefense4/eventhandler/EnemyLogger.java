package com.mechanitis.towerdefense4.eventhandler;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;
import com.mechanitis.towerdefense4.util.WorkshopFileUtils;

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

    public static class LocationSensitiveTurret implements EventHandler<Enemy> {
        private static final int SAFE_DISTANCE_TO_MOVE = 1;
        private int locationCurrentlyBeingTargeted = 1;

        public LocationSensitiveTurret(final int startPoint) {
            locationCurrentlyBeingTargeted = startPoint;
        }

        @Override
        public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
            final int locationToFireAt = enemy.getCurrentLocation();

            final int distanceBetweenTurretLastTargetAndNewTargetLocation = Math.abs(locationToFireAt - locationCurrentlyBeingTargeted);
            //this is bollox - other processors are in the same group as this and you don't know this has been updated
            final boolean missileAlive = !enemy.isDead();
            //now check the enemy is within a nice easy range
            if (distanceBetweenTurretLastTargetAndNewTargetLocation < SAFE_DISTANCE_TO_MOVE && missileAlive) {
                //this basically delays to make it more interesting
                Thread.sleep(0, distanceBetweenTurretLastTargetAndNewTargetLocation);
                locationCurrentlyBeingTargeted = locationToFireAt;
                enemy.shootAt(locationCurrentlyBeingTargeted);
            }
        }

    }
}

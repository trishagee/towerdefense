package com.mechanitis.towerdefense1;

import com.lmax.disruptor.dsl.Disruptor;
import com.mechanitis.towerdefense1.publisher.EnemySpawner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class TowerDefense {
    //TODO: this might need to be changed on your laptop
    private static final Path LOG_FILE_LOCATION = Paths.get(System.getProperty("user.home"), "DisruptorTurretExample");

    private static final int NUMBER_OF_ENEMIES = 2048;
    private static final int RING_BUFFER_SIZE = 1024;

    private final Disruptor<Enemy> disruptor;
    private EnemySpawner enemySpawner;

    private TowerDefense() {
        disruptor = new Disruptor<>(Enemy.EVENT_FACTORY, RING_BUFFER_SIZE, Executors.newSingleThreadExecutor());
        disruptor.handleEventsWith(new Turret());
        enemySpawner = new EnemySpawner();
    }

    private void start() {
        disruptor.start();
        System.out.println("STARTED");
        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_ENEMIES; i++) {
            disruptor.publishEvent(enemySpawner);
        }
        System.out.println("All aliens spawned and sent into the wild");

        disruptor.shutdown();

        final long endTime = System.currentTimeMillis();
        System.out.printf("Time Taken: %d millis%n", (endTime - startTime));
    }

    public static void main(final String[] args) {
        final TowerDefense towerDefense = new TowerDefense();
        towerDefense.start();
        System.exit(0);
    }
}

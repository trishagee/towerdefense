/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mechanitis.towerdefense2;

import com.lmax.disruptor.dsl.Disruptor;
import com.mechanitis.towerdefense2.eventhandler.EnemyLogger;
import com.mechanitis.towerdefense2.publisher.EnemySpawner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

public class TowerDefense {
    //TODO: this might need to be changed on your laptop
    private static final Path LOG_FILE_LOCATION = Paths.get(System.getProperty("user.home"), "DisruptorTurretExample");

    private static final int NUM_EVENT_PROCESSORS = 2;
    private static final int NUMBER_OF_ENEMIES = 2048;
    private static final int RING_BUFFER_SIZE = 1024;

    private final Disruptor<Enemy> disruptor;
    private EnemySpawner enemySpawner;

    private TowerDefense() {
        disruptor = new Disruptor<>(Enemy.EVENT_FACTORY, RING_BUFFER_SIZE, Executors.newFixedThreadPool(NUM_EVENT_PROCESSORS));
        disruptor.handleEventsWith(new EnemyLogger(LOG_FILE_LOCATION), new Turret());
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

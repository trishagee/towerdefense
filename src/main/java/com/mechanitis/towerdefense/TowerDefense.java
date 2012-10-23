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

package com.mechanitis.towerdefense;

import com.lmax.disruptor.dsl.Disruptor;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TowerDefense {
    //TODO: this might need to be changed on your laptop
    private static final Path LOG_FILE_LOCATION = Paths.get(System.getProperty("user.home"), "DisruptorTurretExample");

    private static final int NUMBER_OF_ENEMIES = 2048;
    private static final long RING_BUFFER_SIZE = 1024;

    private final Disruptor disruptor = null;

    private TowerDefense() {
        //TODO: initialise Disruptor and wire it up here
    }

    private void start() {
        //TODO: start the Disruptor here

        //start recording time
        System.out.println("STARTED");
        final long startTime = System.currentTimeMillis();

        //go for it!
        for (int i = 0; i < NUMBER_OF_ENEMIES; i++) {
            //TODO: publish all the events
        }
        System.out.println("All aliens spawned and sent into the wild");

        //TODO: shut down the disruptor here

        //record the end time
        final long endTime = System.currentTimeMillis();
        System.out.println("Time Taken: " + (endTime - startTime) + " millis");
    }

    public static void main(final String[] args) {
        final TowerDefense towerDefense = new TowerDefense();
        towerDefense.start();
        System.exit(0);
    }
}

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

package com.mechanitis.towerdefense2.eventhandler;

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

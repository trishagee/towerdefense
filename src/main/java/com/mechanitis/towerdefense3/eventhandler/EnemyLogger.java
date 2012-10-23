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

package com.mechanitis.towerdefense3.eventhandler;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense3.Enemy;
import com.mechanitis.towerdefense3.util.WorkshopFileUtils;

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

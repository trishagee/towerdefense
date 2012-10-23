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

package com.mechanitis.towerdefense.eventhandler;

import com.mechanitis.towerdefense.Enemy;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JMock.class)
public class EnemyLoggerTest {
    //TODO: this might need to be changed on your laptop
    private static final Path LOG_FILE_LOCATION = Paths.get(System.getProperty("user.home"), "DisruptorTests");
    private static final Path LOG_FILE = LOG_FILE_LOCATION.resolve("towerdefense.log");

    private final Mockery mockery = new Mockery();

    @Before
    public void setUp() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
    }

    @Test
    public void shouldCreateLogDirectoryAndFileOnConstruction() throws Exception {
        assertFalse(Files.exists(LOG_FILE_LOCATION));

        new EnemyLogger(LOG_FILE_LOCATION);

        assertTrue(Files.exists(LOG_FILE_LOCATION));
        assertTrue(Files.exists(LOG_FILE));
    }

    public EnemyLoggerTest() {
        super();
    }

    @Test
    public void shouldDelegatePersistenceOfEnemyDetailsToTheEnemy() throws Exception {
        final EnemyLogger enemyLogger = new EnemyLogger(LOG_FILE_LOCATION);

        final Enemy enemy = mockery.mock(Enemy.class);
        mockery.checking(new Expectations() {
            {
                one(enemy).describeTo(with(any(BufferedWriter.class)));
            }
        });

        enemyLogger.onEvent(enemy, 0L, false);
    }

    @After
    @Before
    public void reset() throws IOException {
        Files.deleteIfExists(LOG_FILE);
        Files.deleteIfExists(LOG_FILE_LOCATION);
    }

}

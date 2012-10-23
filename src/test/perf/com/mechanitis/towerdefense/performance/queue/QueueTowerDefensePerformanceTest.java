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

package com.mechanitis.towerdefense.performance.queue;

import com.mechanitis.towerdefense.performance.Enemy;
import org.junit.Test;

import java.util.concurrent.*;

public class QueueTowerDefensePerformanceTest {
    private static final int NUMBER_OF_RUNS = 3;
    private static final int CAPACITY = 1024 * 8;
    private static final long NUMBER_OF_ENEMIES_TO_PROCESS = 1000L * 1000L * 100L;
    private static final long[] RESULTS = new long[NUMBER_OF_RUNS];

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final ArrayBlockingQueue<Enemy> queue = new ArrayBlockingQueue<>(CAPACITY);
    private final QueueTurret queueTurret = new QueueTurret(queue, NUMBER_OF_ENEMIES_TO_PROCESS - 1);

    @Test
    public void testQueueLatency() throws Exception {
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.gc();
            RESULTS[i] = runQueuePerformance();
        }
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.out.printf("Queue - Time Taken: %,d millis%n", RESULTS[i]);
        }
    }

    private long runQueuePerformance() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        queueTurret.reset(latch);
        final Future<?> future = executor.submit(queueTurret);

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_ENEMIES_TO_PROCESS; i++) {
            queue.put(Enemy.EVENT_FACTORY.newInstance());
        }
        latch.await();
        final long endTime = System.currentTimeMillis();
        queueTurret.halt();
        future.cancel(true);
        return endTime - startTime;
    }
}

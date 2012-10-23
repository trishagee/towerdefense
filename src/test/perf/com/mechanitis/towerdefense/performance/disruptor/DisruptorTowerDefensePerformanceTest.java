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

package com.mechanitis.towerdefense.performance.disruptor;

import com.lmax.disruptor.*;
import com.mechanitis.towerdefense.performance.Enemy;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DisruptorTowerDefensePerformanceTest {
    private static final int NUMBER_OF_RUNS = 3;
    private static final int CAPACITY = 1024 * 8;
    private static final long NUMBER_OF_ENEMIES_TO_PROCESS = 1000L * 1000L * 100L;
    private static final long[] RESULTS = new long[NUMBER_OF_RUNS];

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final EnemySpawner enemySpawner = new EnemySpawner();

    private final DisruptorTurret disruptorTurret = new DisruptorTurret(NUMBER_OF_ENEMIES_TO_PROCESS - 1);
    private final RingBuffer<Enemy> ringBuffer =
            new RingBuffer<>(Enemy.EVENT_FACTORY,
                    new SingleThreadedClaimStrategy(CAPACITY),
                    new YieldingWaitStrategy());
    private final EventPublisher<Enemy> publisher = new EventPublisher<>(ringBuffer);
    private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
    private final BatchEventProcessor<Enemy> batchEventProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, disruptorTurret);

    {
        ringBuffer.setGatingSequences(batchEventProcessor.getSequence());
    }

    @Test
    public void testDisruptorLatency() throws Exception{
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.gc();
            RESULTS[i] = runDisruptorPerformance();
        }
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.out.printf("Disruptor - Time Taken: %,d millis%n", RESULTS[i]);
        }
    }

    private long runDisruptorPerformance() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        disruptorTurret.reset(latch);
        executor.submit(batchEventProcessor);

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_ENEMIES_TO_PROCESS; i++) {
            publisher.publishEvent(enemySpawner);
        }
        latch.await();
        final long endTime = System.currentTimeMillis();
        batchEventProcessor.halt();
        return endTime - startTime;
    }
}

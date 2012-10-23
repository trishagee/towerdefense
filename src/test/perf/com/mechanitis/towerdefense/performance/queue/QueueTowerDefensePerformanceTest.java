package com.mechanitis.towerdefense.performance.queue;

import org.junit.Test;
import com.mechanitis.towerdefense.performance.Enemy;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

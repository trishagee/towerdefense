package com.mechanitis.towerdefense.performance;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventPublisher;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.junit.Assert;
import org.junit.Test;
import com.mechanitis.towerdefense.performance.disruptor.DisruptorTurret;
import com.mechanitis.towerdefense.performance.disruptor.EnemySpawner;
import com.mechanitis.towerdefense.performance.queue.QueueTurret;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class TowerDefensePerformance {
    private static final int NUMBER_OF_RUNS = 3;

    private static final int BUFFER_SIZE = 1024 * 8;
    private static final long NUMBER_OF_ENEMIES_TO_PROCESS = 1000L * 1000L * 100L;
    private static final int NUMBER_OF_PROCESSORS_REQUIRED = 2;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final BlockingQueue<Enemy> blockingQueue = new LinkedBlockingQueue<>(BUFFER_SIZE);
    private final QueueTurret queueTurret = new QueueTurret(blockingQueue, NUMBER_OF_ENEMIES_TO_PROCESS - 1);

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private final RingBuffer<Enemy> ringBuffer = new RingBuffer<>(Enemy.EVENT_FACTORY,
            new SingleThreadedClaimStrategy(BUFFER_SIZE),
            new YieldingWaitStrategy());
    private final EventPublisher<Enemy> publisher = new EventPublisher<>(ringBuffer);
    private final EnemySpawner enemySpawner = new EnemySpawner();
    private final SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
    private final DisruptorTurret disruptorTurret = new DisruptorTurret(NUMBER_OF_ENEMIES_TO_PROCESS - 1);
    private final BatchEventProcessor<Enemy> batchEventProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, disruptorTurret);

    {
        ringBuffer.setGatingSequences(batchEventProcessor.getSequence());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void shouldCompareDisruptorVsQueues() throws Exception {
        final int availableProcessors = Runtime.getRuntime().availableProcessors();
        if (NUMBER_OF_PROCESSORS_REQUIRED > availableProcessors) {
            System.out.print("*** Warning ***: your system has insufficient processors to execute the test efficiently. ");
            System.out.println("Processors required = " + NUMBER_OF_PROCESSORS_REQUIRED + " available = " + availableProcessors);
        }

        final long[] queueOps = new long[NUMBER_OF_RUNS];
        final long[] disruptorOps = new long[NUMBER_OF_RUNS];

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.gc();
            queueOps[i] = runQueuePass();
            System.out.println("Completed BlockingQueue run " + i);
        }

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.gc();
            disruptorOps[i] = runDisruptorPass();
            System.out.println("Completed Disruptor run " + i);
        }

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            System.out.format("%s run %d: BlockingQueue=%,d Disruptor=%,d ops/sec\n",
                    getClass().getSimpleName(), i, queueOps[i], disruptorOps[i]);
        }

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Assert.assertTrue("Performance degraded", disruptorOps[i] > queueOps[i]);
        }
    }

    private long runQueuePass() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        queueTurret.reset(latch);
        final Future<?> future = executor.submit(queueTurret);

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_ENEMIES_TO_PROCESS; i++) {
            blockingQueue.put(Enemy.EVENT_FACTORY.newInstance());
        }
        latch.await();
        final long opsPerSecond = (NUMBER_OF_ENEMIES_TO_PROCESS * 1000L) / (System.currentTimeMillis() - startTime);
        queueTurret.halt();
        future.cancel(true);
        return opsPerSecond;
    }

    private long runDisruptorPass() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        disruptorTurret.reset(latch);
        executor.submit(batchEventProcessor);

        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_ENEMIES_TO_PROCESS; i++) {
            publisher.publishEvent(enemySpawner);
        }
        latch.await();
        final long opsPerSecond = (NUMBER_OF_ENEMIES_TO_PROCESS * 1000L) / (System.currentTimeMillis() - startTime);
        batchEventProcessor.halt();
        return opsPerSecond;
    }

}

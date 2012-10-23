package com.mechanitis.towerdefense3;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventPublisher;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.SingleThreadedClaimStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.mechanitis.towerdefense3.eventhandler.EnemyLogger;
import com.mechanitis.towerdefense3.eventhandler.ResultsCollector;
import com.mechanitis.towerdefense3.publisher.EnemySpawner;
import com.mechanitis.towerdefense3.eventhandler.EnemyLogger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TowerDefense {
    //TODO: this might need to be changed on your laptop
    private static final Path LOG_FILE_LOCATION = Paths.get(System.getProperty("user.home"), "DisruptorTurretExample");

    private static final int NUM_EVENT_PROCESSORS = 3;
    private static final int NUMBER_OF_ENEMIES = 2048;
    private static final int RING_BUFFER_SIZE = 1024;

    private EnemySpawner enemySpawner;
    private RingBuffer<Enemy> ringBuffer;
    private EventPublisher<Enemy> eventPublisher;
    private BatchEventProcessor<Enemy> turretBatchEventProcessor;
    private BatchEventProcessor<Enemy> loggerBatchEventProcessor;
    private ResultsCollector resultsCollector;
    private BatchEventProcessor<Enemy> resultsBatchEventProcessor;

    private TowerDefense() {
        //ring buffer
        ringBuffer = new RingBuffer<>(Enemy.EVENT_FACTORY,
                new SingleThreadedClaimStrategy(RING_BUFFER_SIZE),
                new YieldingWaitStrategy());
        final SequenceBarrier ringBufferSequenceBarrier = ringBuffer.newBarrier();

        //publisher
        enemySpawner = new EnemySpawner();
        eventPublisher = new EventPublisher<>(ringBuffer);

        //event handlers
        //first two in parallel
        final EventHandler<Enemy> turret = new Turret();
        turretBatchEventProcessor = new BatchEventProcessor<>(ringBuffer, ringBufferSequenceBarrier, turret);

        final EnemyLogger logger = new EnemyLogger(LOG_FILE_LOCATION);
        loggerBatchEventProcessor = new BatchEventProcessor<>(ringBuffer, ringBufferSequenceBarrier, logger);

        final SequenceBarrier enemiesProcessedSequenceBarrier =
                ringBuffer.newBarrier(turretBatchEventProcessor.getSequence(), loggerBatchEventProcessor.getSequence());

        //third is after the first two
        resultsCollector = new ResultsCollector(LOG_FILE_LOCATION);
        resultsBatchEventProcessor = new BatchEventProcessor<>(ringBuffer, enemiesProcessedSequenceBarrier, resultsCollector);

        ringBuffer.setGatingSequences(resultsBatchEventProcessor.getSequence());

    }

    private void start() {
        startEventProcessors();

        System.out.println("STARTED");
        final long startTime = System.currentTimeMillis();

        for (int i = 0; i < NUMBER_OF_ENEMIES; i++) {
            eventPublisher.publishEvent(enemySpawner);
        }
        System.out.println("All aliens spawned and sent into the wild");

        shutdown();

        final long endTime = System.currentTimeMillis();
        System.out.printf("Time Taken: %d millis%n", (endTime - startTime));
    }

    private void startEventProcessors() {
        final ExecutorService executor = Executors.newFixedThreadPool(NUM_EVENT_PROCESSORS);
        executor.submit(turretBatchEventProcessor);
        executor.submit(loggerBatchEventProcessor);
        executor.submit(resultsBatchEventProcessor);
    }

    private void shutdown() {
        while (ringBuffer.getCursor() != resultsBatchEventProcessor.getSequence().get())
        {
            //busy spin waiting for the event processors to handle everything
        }
        turretBatchEventProcessor.halt();
        loggerBatchEventProcessor.halt();
        resultsBatchEventProcessor.halt();
    }

    public static void main(final String[] args) {
        final TowerDefense towerDefense = new TowerDefense();
        towerDefense.start();
        System.exit(0);
    }
}

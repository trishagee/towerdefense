package com.mechanitis.towerdefense.performance.disruptor;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense.performance.Enemy;

import java.util.concurrent.CountDownLatch;

public class DisruptorTurret implements EventHandler<Enemy> {
    private final long count;

    private long sequence;
    private CountDownLatch latch;

    public DisruptorTurret(final long count) {
        this.count = count;
    }

    public void reset(final CountDownLatch latch)
    {
        sequence = 0L;
        this.latch = latch;
    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        enemy.shootAt();
        if (sequence++ == count)
        {
            latch.countDown();
        }
    }
}

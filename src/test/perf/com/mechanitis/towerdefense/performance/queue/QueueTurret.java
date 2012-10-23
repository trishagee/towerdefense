package com.mechanitis.towerdefense.performance.queue;

import com.mechanitis.towerdefense.performance.Enemy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public final class QueueTurret implements Runnable
{
    private volatile boolean running;
    private long sequence;
    private CountDownLatch latch;

    private final BlockingQueue<Enemy> blockingQueue;
    private final long count;

    public QueueTurret(final BlockingQueue<Enemy> blockingQueue, final long count)
    {
        this.blockingQueue = blockingQueue;
        this.count = count;
    }

    public void reset(final CountDownLatch latch)
    {
        sequence = 0L;
        this.latch = latch;
    }

    public void halt()
    {
        running = false;
    }

    @Override
    public void run()
    {
        running = true;
        while (true)
        {
            try
            {
                final Enemy enemy = blockingQueue.take();
                enemy.shootAt();

                if (sequence++ == count)
                {
                    latch.countDown();
                }
            }
            catch (InterruptedException ex)
            {
                if (!running)
                {
                    break;
                }
            }
        }
    }
}

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

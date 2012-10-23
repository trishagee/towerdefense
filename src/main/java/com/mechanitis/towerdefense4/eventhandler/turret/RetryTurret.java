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

package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

//ONE RETRY
//Time Taken: 2320 millis
//Number of hits: 141 (out of 2048)

//TWO RETRIES
//Time Taken: 3324 millis
//Number missiles hits: 188 (out of 2048)
public class RetryTurret implements EventHandler<Enemy> {
    private static final int NUMBER_OF_RETRIES = 1;
    private int locationCurrentlyBeingTargeted = -1;

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        final int locationToFireAt = enemy.getCurrentLocation();

        reTargetTurret(locationToFireAt);

        //try to destroy it
        enemy.shootAt(locationCurrentlyBeingTargeted);

        //now let's be smarter about the fact the enemy has moved on
        //retry, just once
        for (int i = NUMBER_OF_RETRIES; i != 0; i--) {
            if (!enemy.isDead()) {
                reTargetTurret(enemy.getCurrentLocation());
                enemy.shootAt(locationCurrentlyBeingTargeted);
            }
        }
    }

    private void reTargetTurret(final int locationToFireAt) throws InterruptedException {
        //this basically delays to make it more interesting
        final int distanceBetweenTurretLastTargetAndNewTargetLocation = Math.abs(locationToFireAt - locationCurrentlyBeingTargeted);
        Thread.sleep(0, distanceBetweenTurretLastTargetAndNewTargetLocation);
        locationCurrentlyBeingTargeted = locationToFireAt;
    }
}

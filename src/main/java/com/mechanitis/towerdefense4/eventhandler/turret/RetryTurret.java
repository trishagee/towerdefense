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

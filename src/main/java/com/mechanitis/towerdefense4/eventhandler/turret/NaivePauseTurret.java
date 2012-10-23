package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

//hit rate of about 70
//Time Taken: 1341 millis
//Number of hits: 77 (out of 2048)
public class NaivePauseTurret implements EventHandler<Enemy> {
    private int locationCurrentlyBeingTargeted = -1;

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        final int locationToFireAt = enemy.getCurrentLocation();

        //let's add a delay to the shooting.  The Turret needs to retarget, for example.
        final int distanceBetweenTurretLastTargetAndNewTargetLocation = Math.abs(locationToFireAt - locationCurrentlyBeingTargeted);
//        System.out.printf("NaivePauseTurret re-targeting, moving %d metres%n", distanceBetweenTurretLastTargetAndNewTargetLocation);
        Thread.sleep(0, distanceBetweenTurretLastTargetAndNewTargetLocation);
        locationCurrentlyBeingTargeted = locationToFireAt;

        enemy.shootAt(locationCurrentlyBeingTargeted);
    }
}

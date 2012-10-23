package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

public class MultiShotTurret implements EventHandler<Enemy> {
    private int locationCurrentlyBeingTargeted = -1;

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        final int locationToFireAt = enemy.getCurrentLocation();

        //let's add a delay to the shooting.  The Turret needs to move its firing thingie.
        final int distanceBetweenTurretLastTargetAndNewTargetLocation = Math.abs(locationToFireAt - locationCurrentlyBeingTargeted);
        Thread.sleep(0, distanceBetweenTurretLastTargetAndNewTargetLocation);
        locationCurrentlyBeingTargeted = locationToFireAt;

        enemy.shootAt(locationCurrentlyBeingTargeted);
        enemy.shootAt(locationCurrentlyBeingTargeted+1);
        enemy.shootAt(locationCurrentlyBeingTargeted+2);
        enemy.shootAt(locationCurrentlyBeingTargeted+3);
        enemy.shootAt(locationCurrentlyBeingTargeted+4);
    }
}

//shoot at locations 0, 1, 2
//Time Taken: 1376 millis
//Number of missiles hit: 1045
// 2048 targeted (51.025391%), 2048 total fired(0%)

//shoot at locations 0, 1, 2, 3, 4
//Time Taken: 1376 millis
//Number of missiles hit: 1045
// 2048 targeted (51.025391%), 2048 total fired(0%)
//Time Taken: 1227 millis
//Number of missiles hit: 1903
// 2048 targeted (92.919922%), 2048 total fired(0%)

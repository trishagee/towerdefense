package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

public class SequenceSensitiveTurret implements EventHandler<Enemy> {
    ///ooops - if you set this to 1, you get the same as a delayed turret
    private static final int MAGIC_MOD_FOR_SEQUENCE = 2;

    private int locationCurrentlyBeingTargeted = -1;
    private final int modValuesThatICareAbout;

    public SequenceSensitiveTurret(final int modValuesThatICareAbout) {
        this.modValuesThatICareAbout = modValuesThatICareAbout;
    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        if (sequenceNumber % MAGIC_MOD_FOR_SEQUENCE == modValuesThatICareAbout) {
            final int locationToFireAt = enemy.getCurrentLocation();

            final int distanceBetweenTurretLastTargetAndNewTargetLocation = Math.abs(locationToFireAt - locationCurrentlyBeingTargeted);
            Thread.sleep(0, distanceBetweenTurretLastTargetAndNewTargetLocation);
            locationCurrentlyBeingTargeted = locationToFireAt;

            enemy.shootAt(locationCurrentlyBeingTargeted);
        }
    }
}

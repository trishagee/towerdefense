package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

public class SequenceSensitiveFastTurret implements EventHandler<Enemy> {
    private static final int MAGIC_MOD_FOR_SEQUENCE = 1;

    private final int modValuesThatICareAbout;

    public SequenceSensitiveFastTurret(final int modValuesThatICareAbout) {
        this.modValuesThatICareAbout = modValuesThatICareAbout;
    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        if (sequenceNumber % MAGIC_MOD_FOR_SEQUENCE == modValuesThatICareAbout) {
            enemy.shootAt(enemy.getCurrentLocation());
        }
    }
}

package com.mechanitis.towerdefense.performance.disruptor;

import com.lmax.disruptor.EventTranslator;
import com.mechanitis.towerdefense.performance.Enemy;

public class EnemySpawner implements EventTranslator<Enemy> {
    @Override
    public void translateTo(final Enemy enemy, final long sequenceNumber) {
        enemy.respawn();
    }
}

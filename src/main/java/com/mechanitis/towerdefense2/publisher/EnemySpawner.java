package com.mechanitis.towerdefense2.publisher;

import com.lmax.disruptor.EventTranslator;
import com.mechanitis.towerdefense2.Enemy;

public class EnemySpawner implements EventTranslator<Enemy> {
    @Override
    public void translateTo(Enemy event, long sequence) {
        System.out.println("Repawning enemy");
        event.respawn();
    }
}

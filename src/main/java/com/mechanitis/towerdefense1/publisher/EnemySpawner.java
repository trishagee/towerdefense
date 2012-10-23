package com.mechanitis.towerdefense1.publisher;

import com.lmax.disruptor.EventTranslator;
import com.mechanitis.towerdefense1.Enemy;

public class EnemySpawner implements EventTranslator<Enemy> {
    @Override
    public void translateTo(Enemy event, long sequence) {
        System.out.println("Repawning enemy");
        event.respawn();
    }
}

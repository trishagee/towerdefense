package com.mechanitis.towerdefense.publisher;

import com.lmax.disruptor.EventTranslator;
import com.mechanitis.towerdefense.Enemy;

public class EnemySpawner implements EventTranslator<Enemy> {
    @Override
    public void translateTo(Enemy event, long sequence) {
        System.out.println("Repawning enemy");
        //TODO: implement this!
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

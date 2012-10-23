package com.mechanitis.towerdefense4.publisher;

import com.lmax.disruptor.EventTranslator;
import com.mechanitis.towerdefense4.Enemy;

import java.util.Random;

public class EnemySpawner implements EventTranslator<Enemy> {

    private static final int UPPER_BOUND = 4000;
    private static final int LOWER_BOUND = 500;

    private final Random randomGenerator = new Random();
    private final int distanceToTargetInMetres;

    public EnemySpawner(final int distanceToTargetInMetres) {
        this.distanceToTargetInMetres = distanceToTargetInMetres;
    }

    @Override
    public void translateTo(final Enemy enemy, final long sequenceNumber) {
        enemy.respawn(randomiseSpeed(), distanceToTargetInMetres);
    }

    private int randomiseSpeed() {
        return randomGenerator.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }
}

package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

public class Turret implements EventHandler<Enemy> {
    @Override
    public void onEvent(Enemy enemy, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(sequence + " An enemy on the horizon! " + enemy);
        enemy.shootAt(enemy.getCurrentLocation());
    }
}

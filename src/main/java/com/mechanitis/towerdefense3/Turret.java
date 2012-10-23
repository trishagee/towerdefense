package com.mechanitis.towerdefense3;

import com.lmax.disruptor.EventHandler;

public class Turret implements EventHandler<Enemy> {
    @Override
    public void onEvent(Enemy enemy, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(sequence + " An enemy on the horizon! " + enemy);
        enemy.shootAt();
    }
}

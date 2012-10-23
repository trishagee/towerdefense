package com.mechanitis.towerdefense;

import com.lmax.disruptor.EventHandler;

public class Turret implements EventHandler<Enemy> {
    @Override
    public void onEvent(Enemy event, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(sequence + " An enemy on the horizon! " + event);
        //TODO: implement!
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

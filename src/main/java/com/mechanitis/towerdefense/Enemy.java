package com.mechanitis.towerdefense;

import com.lmax.disruptor.EventFactory;

import java.io.BufferedWriter;

public class Enemy {
    //TODO: design your deadly enemy however you like

    public void shootAt() {
        System.out.println("Someone's firing at me!");
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public long getAge() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public long getTotalDistanceTravelledInMetres() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isDead() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void respawn() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void describeTo(BufferedWriter writer) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static class EnemyEventFactory implements EventFactory<Enemy> {
        @Override
        public Enemy newInstance() {
            //TODO: implement this
            throw new UnsupportedOperationException("Not implemented yet");
        }
    }
}

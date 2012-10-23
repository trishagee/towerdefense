package com.mechanitis.towerdefense3;

import com.lmax.disruptor.EventFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class Enemy {
    //TODO: design your deadly enemy however you like
    public static final EventFactory<Enemy> EVENT_FACTORY = new EnemyEventFactory();
    private boolean dead = false;
    private long age = 0;
    private long distance = 0;

    public void shootAt() {
        System.out.println("Someone's firing at me!");
        dead = true;
        System.out.println("And I died...");
    }

    public long getAge() {
        return age;
    }

    public long getTotalDistanceTravelledInMetres() {
        return distance;
    }

    public boolean isDead() {
        return dead;
    }

    public void respawn() {
        dead = false;
    }

    public void describeTo(BufferedWriter writer) throws IOException {
        writer.write(this.toString());
    }

    private static class EnemyEventFactory implements EventFactory<Enemy> {
        @Override
        public Enemy newInstance() {
            return new Enemy();
        }
    }
}

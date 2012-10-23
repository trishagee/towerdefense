package com.mechanitis.towerdefense.performance;

import com.lmax.disruptor.EventFactory;

public class Enemy {
    private boolean dead;
    public static final EventFactory<Enemy> EVENT_FACTORY = new EnemyEventFactory();

    public void shootAt() {
        dead = true;
    }

    public void respawn() {
        dead = false;
    }

    public boolean isDead() {
        return dead;
    }

    private static class EnemyEventFactory implements EventFactory<Enemy> {
        @Override
        public Enemy newInstance() {
            return new Enemy();
        }
    }
}

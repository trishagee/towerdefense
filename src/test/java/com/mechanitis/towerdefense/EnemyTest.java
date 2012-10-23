package com.mechanitis.towerdefense;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EnemyTest {
    @Test
    //in the simplest case, these should just die
    public void shouldDieWhenShotAt() throws Exception {
        final Enemy enemy = new Enemy();
        enemy.shootAt();
        assertTrue(enemy.isDead());
    }

    @Test
    public void shouldReviveWhenResetCalled() throws Exception {
        final Enemy enemy = new Enemy();
        enemy.respawn();
        assertFalse(enemy.isDead());
    }

}

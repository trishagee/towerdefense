package com.mechanitis.towerdefense;

import com.mechanitis.towerdefense.publisher.EnemySpawner;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class EnemySpawnerTest {
    private final Mockery mockery = new Mockery();

    @Before
    public void setUp() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
    }

    @Test
    public void shouldResetEnemies() throws Exception {
        final EnemySpawner enemySpawner = new EnemySpawner();

        //slightly odd syntax because the Disruptor allows garbage-collector-friendly algorithms
        final Enemy enemyForRegeneration = mockery.mock(Enemy.class);
        mockery.checking(new Expectations() {
            {
                one(enemyForRegeneration).respawn();
            }
        });

        enemySpawner.translateTo(enemyForRegeneration, 0);
    }

}

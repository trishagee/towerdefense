/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mechanitis.towerdefense;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class TurretTest {
    private final Mockery mockery = new Mockery();

    @Before
    public void setUp() {
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
    }

    @Test
    //in the simplest case, all that matters is that the enemy is "processed"
    public void shouldFireAtEnemies() throws Exception {
        final Turret turret = new Turret();
        final Enemy enemy = mockery.mock(Enemy.class);

        mockery.checking(new Expectations() {
            {
                one(enemy).shootAt();
            }
        });

        turret.onEvent(enemy, 0, false);
    }
}

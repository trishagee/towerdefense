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

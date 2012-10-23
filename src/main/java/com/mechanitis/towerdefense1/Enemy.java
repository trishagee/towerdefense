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

package com.mechanitis.towerdefense1;

import com.lmax.disruptor.EventFactory;

import java.io.BufferedWriter;

public class Enemy {
    //TODO: design your deadly enemy however you like
    public static final EventFactory<Enemy> EVENT_FACTORY = new EnemyEventFactory();
    private boolean dead;

    public void shootAt() {
        System.out.println("Someone's firing at me!");
        dead = true;
    }

    public long getAge() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public long getTotalDistanceTravelledInMetres() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isDead() {
        return dead;
    }

    public void respawn() {
        dead = false;
    }

    public void describeTo(BufferedWriter writer) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private static class EnemyEventFactory implements EventFactory<Enemy> {
        @Override
        public Enemy newInstance() {
            return new Enemy();
        }
    }
}

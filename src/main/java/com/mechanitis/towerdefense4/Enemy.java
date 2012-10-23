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

package com.mechanitis.towerdefense4;

import com.lmax.disruptor.EventFactory;

import java.io.BufferedWriter;
import java.io.IOException;

import static com.mechanitis.towerdefense4.util.Physics.getDistanceTravelledInMetres;
import static com.mechanitis.towerdefense4.util.Physics.getTimeInMillisToTravelDistanceAtSpeed;

public class Enemy {
    public static final EventFactory<Enemy> EVENT_FACTORY = new EnemyEventFactory();

    private long timeSpawned;
    private long timeKilled;

    private int speed;
    private int distanceToTargetInMetres;

    private boolean destroyed;
    private boolean shotAt;

    public void respawn(final int speedInMetresPerSecond, final int distanceToTargetInMetres) {
        this.speed = speedInMetresPerSecond;
        this.distanceToTargetInMetres = distanceToTargetInMetres;
        this.destroyed = false;
        timeSpawned = System.currentTimeMillis();
        timeKilled = 0;
    }

    public void shootAt(final int location) {
        shotAt = true;
        if (location == getCurrentLocation()) {
            kill();
        }
    }

    private void kill() {
        timeKilled = System.currentTimeMillis();
        destroyed = true;
    }

    public void describeTo(final BufferedWriter writer) throws IOException {
        writer.write(speed + "m/s");
    }

    public boolean isDead() {
        return destroyed;
    }

    public boolean wasShotAt() {
        return shotAt;
    }

    public long getAge() {
        if (isDead()) {
            return timeKilled - timeSpawned;
        } else {
            return getTimeInMillisToTravelDistanceAtSpeed(distanceToTargetInMetres, speed);
        }
    }

    public int getTotalDistanceTravelledInMetres() {
        if (isDead()) {
            return getDistanceTravelledInMetres(getAge(), speed);
        } else {
            return distanceToTargetInMetres;
        }
    }

    public int getCurrentLocation() {
        final long timeInFlight = System.currentTimeMillis() - timeSpawned;
        return getDistanceTravelledInMetres(timeInFlight, speed);
    }

    private static class EnemyEventFactory implements EventFactory<Enemy> {
        @Override
        public Enemy newInstance() {
            return new Enemy();
        }
    }
}

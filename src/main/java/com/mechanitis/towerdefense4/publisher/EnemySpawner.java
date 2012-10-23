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

package com.mechanitis.towerdefense4.publisher;

import com.lmax.disruptor.EventTranslator;
import com.mechanitis.towerdefense4.Enemy;

import java.util.Random;

public class EnemySpawner implements EventTranslator<Enemy> {

    private static final int UPPER_BOUND = 4000;
    private static final int LOWER_BOUND = 500;

    private final Random randomGenerator = new Random();
    private final int distanceToTargetInMetres;

    public EnemySpawner(final int distanceToTargetInMetres) {
        this.distanceToTargetInMetres = distanceToTargetInMetres;
    }

    @Override
    public void translateTo(final Enemy enemy, final long sequenceNumber) {
        enemy.respawn(randomiseSpeed(), distanceToTargetInMetres);
    }

    private int randomiseSpeed() {
        return randomGenerator.nextInt(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND;
    }
}

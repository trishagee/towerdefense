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

package com.mechanitis.towerdefense4.eventhandler.turret;

import com.lmax.disruptor.EventHandler;
import com.mechanitis.towerdefense4.Enemy;

public class SequenceSensitiveFastTurret implements EventHandler<Enemy> {
    private static final int MAGIC_MOD_FOR_SEQUENCE = 1;

    private final int modValuesThatICareAbout;

    public SequenceSensitiveFastTurret(final int modValuesThatICareAbout) {
        this.modValuesThatICareAbout = modValuesThatICareAbout;
    }

    @Override
    public void onEvent(final Enemy enemy, final long sequenceNumber, final boolean endOfBatch) throws Exception {
        if (sequenceNumber % MAGIC_MOD_FOR_SEQUENCE == modValuesThatICareAbout) {
            enemy.shootAt(enemy.getCurrentLocation());
        }
    }
}

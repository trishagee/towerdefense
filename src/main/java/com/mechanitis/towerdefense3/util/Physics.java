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

package com.mechanitis.towerdefense3.util;

public class Physics {
    public static final int MULTIPLIER_FROM_SECONDS_TO_MILLIS = 1_000;

    public static int getDistanceTravelledInMetres(final long timeInMillis, final int speedInMetresPerSecond) {
        return (int) (speedInMetresPerSecond * timeInMillis) / MULTIPLIER_FROM_SECONDS_TO_MILLIS;
    }

    public static long getTimeInMillisToTravelDistanceAtSpeed(final int distanceToTargetInMetres, final int speed) {
        return (distanceToTargetInMetres / speed) * MULTIPLIER_FROM_SECONDS_TO_MILLIS;
    }
}

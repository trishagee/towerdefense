package com.mechanitis.towerdefense4.util;

public class Physics {
    public static final int MULTIPLIER_FROM_SECONDS_TO_MILLIS = 1_000;

    public static int getDistanceTravelledInMetres(final long timeInMillis, final int speedInMetresPerSecond) {
        return (int) (speedInMetresPerSecond * timeInMillis) / MULTIPLIER_FROM_SECONDS_TO_MILLIS;
    }

    public static long getTimeInMillisToTravelDistanceAtSpeed(final int distanceToTargetInMetres, final int speed) {
        return (distanceToTargetInMetres / speed) * MULTIPLIER_FROM_SECONDS_TO_MILLIS;
    }
}

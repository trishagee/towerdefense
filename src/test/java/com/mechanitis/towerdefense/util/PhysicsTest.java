package com.mechanitis.towerdefense.util;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PhysicsTest {
    @Test
    public void shouldCorrectlyCalculateDistanceInMetresFromSpeedAndTime() throws Exception {
        final int speedInMetresPerSecond = 25;
        final int timeTakenInSeconds = 4;

        final int expectedDistance = 100; // 4 * 25

        final long timeTakenInMillis = timeTakenInSeconds * 1000;
        final int actualDistanceTravelledInMetres = Physics.getDistanceTravelledInMetres(timeTakenInMillis, speedInMetresPerSecond);
        assertThat(actualDistanceTravelledInMetres, is(expectedDistance));
    }

    @Test
    public void shouldCorrectlyCalculateTimeInMillisToTravelDistanceAtSpeed() throws Exception {
        final int speedInMetresPerSecond = 30;
        final int distanceTravelledInMetres = 90;

        final int expectedTimeInSeconds = 3; // 90 / 30

        final long expectedTimeInMillis = expectedTimeInSeconds * 1000;

        final long actualTimeInMillis = Physics.getTimeInMillisToTravelDistanceAtSpeed(distanceTravelledInMetres, speedInMetresPerSecond);

        assertThat(actualTimeInMillis, is(expectedTimeInMillis));
    }
}

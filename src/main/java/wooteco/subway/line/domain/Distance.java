package wooteco.subway.line.domain;

import wooteco.subway.exception.InternalLogicException;
import wooteco.subway.exception.addition.LineDistanceException;

public class Distance {
    private static final int DEFAULT_FARE_RANGE = 10;
    private static final int FIRST_FARE_RANGE_DISTANCE = 50;
    private static final int MIN_POSITIVE = 1;

    public static final int DEFAULT_TO_FIRST_RANGE_DISTANCE = FIRST_FARE_RANGE_DISTANCE - DEFAULT_FARE_RANGE;

    private final int distance;

    public Distance(int distance) {
        validateLineDistance(distance);
        this.distance = distance;
    }

    private void validateLineDistance(int distance) {
        if (distance < MIN_POSITIVE) {
            throw new LineDistanceException();
        }
    }

    public int toInt() {
        return distance;
    }

    public boolean isDefaultRange() {
        return distance <= DEFAULT_FARE_RANGE;
    }

    public boolean isFirstRange() {
        return DEFAULT_FARE_RANGE < distance
                && distance <= FIRST_FARE_RANGE_DISTANCE;
    }

    public int firstDistanceRange() {
        return distance - DEFAULT_FARE_RANGE;
    }

    public int secondDistanceRange() {
        if (distance < FIRST_FARE_RANGE_DISTANCE) {
            throw new InternalLogicException();
        }
        return distance - FIRST_FARE_RANGE_DISTANCE;
    }
}

package wooteco.subway.line.domain;

import wooteco.subway.exception.addition.LineDistanceException;

public class Distance {
    private static final int MIN_POSITIVE = 1;

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

    public boolean isLessOrEqualThan(int value) {
        return distance <= value;
    }

    public boolean isLessThan(int value) {
        return distance < value;
    }

    public boolean isMoreThan(int value) {
        return distance > value;
    }

    public int subtract(int value) {
        return distance - value;
    }
}

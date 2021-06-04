package wooteco.subway.path.domain;

import java.util.Objects;
import wooteco.subway.exception.InvalidFareException;

public class Fare {

    private final int value;

    public Fare(int value) {
        this.value = value;
        validateNegative(this.value);
    }

    private void validateNegative(int value) {
        if (value < 0) {
            throw new InvalidFareException();
        }
    }

    public Fare addExtraFare(Fare fare) {
        return new Fare(value + fare.value);
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

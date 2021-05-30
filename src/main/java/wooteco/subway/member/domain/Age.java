package wooteco.subway.member.domain;

import wooteco.subway.exception.InvalidAgeException;

public class Age {

    private final int value;

    public Age(int value) {
        this.value = value;
        validateNegative(this.value);
    }

    private void validateNegative(int value) {
        if (value < 0) {
            throw new InvalidAgeException();
        }
    }

    public int getValue() {
        return value;
    }
}

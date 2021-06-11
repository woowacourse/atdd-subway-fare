package wooteco.subway.member.domain;

import java.util.Objects;
import wooteco.subway.exception.InvalidAgeException;

public class Age {

    private final Integer value;

    public Age(Integer value) {
        this.value = value;
        validateNull(this.value);
        validateNegative(this.value);
    }

    private void validateNull(Integer value) {
        if (Objects.isNull(value)) {
            throw new InvalidAgeException();
        }
    }

    private void validateNegative(Integer value) {
        if (value < 0) {
            throw new InvalidAgeException();
        }
    }

    public int getValue() {
        return value;
    }
}

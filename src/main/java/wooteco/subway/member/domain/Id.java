package wooteco.subway.member.domain;

import java.util.Objects;
import wooteco.subway.exception.InvalidIdException;

public class Id {

    private final Long value;

    public Id(Long value) {
        this.value = value;
        validateNull(this.value);
        validatePositive(this.value);
    }

    private void validateNull(Long value) {
        if (Objects.isNull(value)) {
            throw new InvalidIdException();
        }
    }

    private void validatePositive(Long value) {
        if (value < 1) {
            throw new InvalidIdException();
        }
    }

    public Long getValue() {
        return value;
    }
}

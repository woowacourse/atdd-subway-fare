package wooteco.subway.line.domain;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Id id = (Id) o;
        return Objects.equals(value, id.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

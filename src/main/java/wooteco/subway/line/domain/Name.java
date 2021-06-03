package wooteco.subway.line.domain;

import java.util.Objects;
import wooteco.subway.exception.InvalidNameException;

public class Name {

    private static final String PATTERN = "^[가-힣0-9]*$";

    private final String value;

    public Name(String value) {
        this.value = value;
        validateNull(this.value);
        validateForm(this.value);
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new InvalidNameException();
        }
    }

    private void validateForm(String value) {
        if (value.matches(PATTERN)) {
            return;
        }

        throw new InvalidNameException();
    }

    public String getValue() {
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
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

package wooteco.subway.member.domain;

import java.util.Objects;
import wooteco.subway.exception.InvalidEmailException;

public class Email {

    private static final String PATTERN = "^[a-zA-Z0-9]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    private final String value;

    public Email(String value) {
        this.value = value;
        validateNull(this.value);
        validateForm(this.value);
    }

    private void validateNull(String value) {
        if (Objects.isNull(value)) {
            throw new InvalidEmailException();
        }
    }

    private void validateForm(String value) {
        if (value.matches(PATTERN)) {
            return;
        }

        throw new InvalidEmailException();
    }

    public String getValue() {
        return value;
    }
}

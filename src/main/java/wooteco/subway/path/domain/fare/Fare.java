package wooteco.subway.path.domain.fare;

import java.util.Objects;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.path.domain.fare.discountrule.DiscountRule;

public class Fare {

    public static final Fare ZERO_FARE = new Fare(0);
    public static final Fare DEFAULT_FARE = new Fare(1250);

    private final int value;

    public Fare(int value) {
        validatePositiveOrZero(value);
        this.value = value;
    }

    private void validatePositiveOrZero(int value) {
        if (value < 0) {
            throw new ValidationFailureException("요금은 음수일 수 없습니다.");
        }
    }

    public Fare addFare(Fare fare) {
        return new Fare(value + fare.value);
    }

    public Fare minus(int value) {
        return new Fare(this.value - value);
    }

    public Fare multiplyScale(double scale) {
        return new Fare(
            (int) (value * scale)
        );
    }

    public Fare applyDiscountRule(DiscountRule discountRule) {
        return discountRule.apply(this);
    }

    public int value() {
        return this.value;
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

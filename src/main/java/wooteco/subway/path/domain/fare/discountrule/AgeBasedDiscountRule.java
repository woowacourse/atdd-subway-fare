package wooteco.subway.path.domain.fare.discountrule;

import java.util.Arrays;
import java.util.function.IntPredicate;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.path.domain.fare.Fare;

public enum AgeBasedDiscountRule implements DiscountRule {
    INFANT(0, 350, age -> (0 <= age) && (age < 6)),
    CHILD(0.5, 350, age -> (6 <= age) && (age < 13)),
    JUVENILE(0.8, 350, age -> (13 <= age) && (age < 19)),
    ADULT(1.0, 0, age -> (19 <= age));

    private final double appliedRate;
    private final int deductedFare;
    private final IntPredicate matchedFilter;

    AgeBasedDiscountRule(double appliedRate, int deductedFare, IntPredicate matchedFilter) {
        this.appliedRate = appliedRate;
        this.deductedFare = deductedFare;
        this.matchedFilter = matchedFilter;
    }

    public static AgeBasedDiscountRule from(int age) {
        validatePositiveOrZero(age);
        return Arrays.stream(AgeBasedDiscountRule.values())
            .filter(rule -> rule.matchedFilter.test(age))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 나이가 존재하지 않습니다."));
    }

    private static void validatePositiveOrZero(int age) {
        if (age < 0) {
            throw new ValidationFailureException("나이는 음수일 수 없습니다.");
        }
    }

    @Override
    public Fare apply(Fare fare) {
        return fare.minus(deductedFare)
            .multiplyScale(appliedRate);
    }
}

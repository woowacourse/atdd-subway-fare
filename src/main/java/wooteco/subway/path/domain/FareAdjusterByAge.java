package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.BinaryOperator;

import static wooteco.subway.path.domain.FareAdjusterByAge.Constants.*;

public enum FareAdjusterByAge {
    PRE_SCHOOLED(0, 6, (fare, lineFare) -> 0),
    SCHOOL_AGED(6, 13, (fare, lineFare) -> (int) (((fare + lineFare) - COMMON_DISCOUNT) * SCHOOL_AGED_DISCOUNT_RATE)),
    ADOLESCENT(13, 19, (fare, lineFare) -> (int) (((fare + lineFare) - COMMON_DISCOUNT) * ADOLESCENT_DISCOUNT_RATE));

    private final int minInclusive;
    private final int maxExclusive;
    private final BinaryOperator<Integer> adjuster;

    FareAdjusterByAge(int minInclusive, int maxExclusive, BinaryOperator<Integer> adjuster) {
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
        this.adjuster = adjuster;
    }

    public static int of(int age, int fare, int lineFare) {
        return Arrays.stream(values())
                .filter(ageRange -> ageRange.minInclusive <= age && age < ageRange.maxExclusive)
                .map(calculator -> calculator.adjuster.apply(fare, lineFare))
                .findAny()
                .orElseGet(() -> fare + lineFare);
    }

    static class Constants {
        static final int COMMON_DISCOUNT = 350;
        static final double SCHOOL_AGED_DISCOUNT_RATE = 0.5;
        static final double ADOLESCENT_DISCOUNT_RATE = 0.8;
    }
}

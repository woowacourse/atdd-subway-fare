package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum FareAdjusterByAge {
    PRE_SCHOOLED(0, 6, getIntegerBinaryOperator(0)),
    SCHOOL_AGED(6, 13, getIntegerBinaryOperator(0.5)),
    ADOLESCENT(13, 19, getIntegerBinaryOperator(0.8));

    private final int minInclusive;
    private final int maxExclusive;
    private final BinaryOperator<Integer> adjuster;
    FareAdjusterByAge(int minInclusive, int maxExclusive, BinaryOperator<Integer> adjuster) {
        this.minInclusive = minInclusive;
        this.maxExclusive = maxExclusive;
        this.adjuster = adjuster;
    }

    private static BinaryOperator<Integer> getIntegerBinaryOperator(double rate) {
        return (fare, lineFare) -> (int) (((fare + lineFare) - 350) * rate);
    }

    public static int of(int age, int fare, int lineFare) {
        return Arrays.stream(values())
                .filter(ageRange -> ageRange.minInclusive <= age && age < ageRange.maxExclusive)
                .map(calculator -> calculator.adjuster.apply(fare, lineFare))
                .findAny()
                .orElse(fare + lineFare);
    }
}

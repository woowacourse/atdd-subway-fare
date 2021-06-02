package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.BinaryOperator;

public enum FareAdjusterByAge {
    PRE_SCHOOLED(0, Constants.PRE_SCHOOL_MAX_BOUNDARY, getIntegerBinaryOperator(0)),
    SCHOOL_AGED(Constants.PRE_SCHOOL_MAX_BOUNDARY, Constants.SCHOOL_AGED_MAX_BOUNDARY, getIntegerBinaryOperator(Constants.SCHOOL_AGED_RATE)),
    ADOLESCENT(Constants.SCHOOL_AGED_MAX_BOUNDARY, Constants.ADOLESCENT_MAX_BOUNDARY, getIntegerBinaryOperator(Constants.ADOLESCENT_RATE));

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

    private static class Constants {
        public static final int PRE_SCHOOL_MAX_BOUNDARY = 6;
        public static final int SCHOOL_AGED_MAX_BOUNDARY = 13;
        public static final int ADOLESCENT_MAX_BOUNDARY = 19;
        public static final double SCHOOL_AGED_RATE = 0.5;
        public static final double ADOLESCENT_RATE = 0.8;
    }
}

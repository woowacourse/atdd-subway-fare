package wooteco.subway.fare.domain;

import java.util.Arrays;

public enum FareByAge {
    CHILDREN(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    OTHER(0, 0, 0);

    private static final int deductibleFare = 350;

    private final int from;
    private final int to;
    private final double discountRate;

    FareByAge(int from, int to, double discountRate) {
        this.from = from;
        this.to = to;
        this.discountRate = discountRate;
    }

    public static int calculate(int memberAge, int fareWithoutDiscount) {
        final FareByAge agePolicy = Arrays.stream(values())
                .filter(fareByAge -> fareByAge.from <= memberAge && memberAge < fareByAge.to)
                .findAny()
                .orElse(OTHER);

        return (int) ((fareWithoutDiscount - deductibleFare) * (1 - agePolicy.discountRate)) + deductibleFare;
    }
}

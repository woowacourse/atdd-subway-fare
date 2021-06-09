package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FarePolicy {
    FIRST_BOUND(1, 0, (distance) -> distance <= 10),
    SECOND_BOUND(5, 100, (distance) -> distance > 10 && distance <= 50),
    THIRD_BOUND(8, 100, (distance) -> distance > 50);

    private final int kilometer;
    private final int extraFare;
    private final Predicate<Integer> distancePolicy;

    FarePolicy(int kilometer, int extraFare, Predicate<Integer> distancePolicy) {
        this.kilometer = kilometer;
        this.extraFare = extraFare;
        this.distancePolicy = distancePolicy;
    }

    public static FarePolicy findBound(int distance) {
        return Arrays.stream(values())
                .filter(value -> value.distancePolicy.test(distance))
                .findAny()
                .orElse(FIRST_BOUND);
    }

    public int getKilometer() {
        return kilometer;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public Predicate<Integer> getDistancePolicy() {
        return distancePolicy;
    }
}

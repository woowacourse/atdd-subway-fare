package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum DistanceCharge {
    FIRST_OVER_CHARGE((distance) -> (int) ((Math.ceil((distance - 10) / 5) + 1) * 100), (distance) -> distance > 10 && distance <= 50),
    SECOND_OVER_CHARGE((distance) -> 800 + (int) ((Math.ceil((distance - 50) / 8) + 1) * 100), (distance) -> 50 < distance),
    NO_OVER_CHARGE((distance) -> 0, (distance) -> true);

    private final IntFunction<Integer> calculateOverCharge;
    private final IntPredicate chargeScheme;

    DistanceCharge(final IntFunction<Integer> calculateOverCharge, final IntPredicate chargeScheme) {
        this.calculateOverCharge = calculateOverCharge;
        this.chargeScheme = chargeScheme;
    }

    public static int getDistanceCharge(final int distance) {
        return Arrays.stream(values())
                .filter(it -> it.chargeScheme.test(distance))
                .findFirst()
                .map(it -> it.calculateOverCharge.apply(distance))
                .orElseThrow(IllegalArgumentException::new);
    }
}

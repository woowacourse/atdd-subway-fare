package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceCharge {
    FIRST_OVER_CHARGE(Distance::calculateFirstOverCharge, Distance::isFirstOverCharge),
    SECOND_OVER_CHARGE(Distance::calculateSecondOverCharge, Distance::isSecondOverCharge),
    NONE((distance) -> 0, Distance::isNoOverCharge);

    private final Function<Distance, Integer> calculateOverCharge;
    private final Predicate<Distance> chargeScheme;

    DistanceCharge(final Function<Distance, Integer> calculateOverCharge, final Predicate<Distance> chargeScheme) {
        this.calculateOverCharge = calculateOverCharge;
        this.chargeScheme = chargeScheme;
    }

    public static int calculateDistanceCharge(final Distance distance) {
        return Arrays.stream(values())
                .filter(it -> it.chargeScheme.test(distance))
                .findFirst()
                .map(it -> it.calculateOverCharge.apply(distance))
                .orElseThrow(IllegalArgumentException::new);
    }
}

package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum FareByDistance {
    DEFAULT_DISTANCE(1, 0, 0, (distance) -> distance <= 10),
    EXTRA_DISTANCE(5, 10, 1250, (distance) -> distance > 10 && distance <= 50),
    FAR_DISTANCE(8, 50, 2250, (distance) -> distance > 50);

    private final int unitDistance;
    private final int extraDistance;
    private final int previousFare;
    private final Predicate<Integer> match;

    FareByDistance(int unitDistance, int extraDistance, int previousFare,
        Predicate<Integer> match) {
        this.unitDistance = unitDistance;
        this.extraDistance = extraDistance;
        this.previousFare = previousFare;
        this.match = match;
    }

    public static int calculate(Integer distance) {
        return Arrays.stream(values())
            .filter(fareByDistance -> fareByDistance.match.test(distance))
            .map(fareByDistance -> fareByDistance.calculateExtraFare(distance))
            .findAny()
            .orElseThrow(RuntimeException::new);
    }

    private int calculateExtraFare(int distance) {
        if(distance <= 10) {
            return 1250;
        }
        return previousFare + (int)((Math.ceil((distance - extraDistance - 1) / unitDistance) +1) * 100);
    }
}

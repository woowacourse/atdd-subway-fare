package wooteco.subway.path.domain.fare.distance;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceType {
    UNDER_TEN(new UnderTenStrategy(), (distance) -> distance <= 10),
    TEN_TO_FIFTY(new TenToFiftyStrategy(), (distance) -> distance > 10 && distance <= 50),
    OVER_FIFTY(new OverFiftyStrategy(), (distance) -> distance > 50);

    private final DistanceStrategy strategy;
    private final Predicate<Integer> match;

    DistanceType(DistanceStrategy strategy, Predicate<Integer> match) {
        this.strategy = strategy;
        this.match = match;
    }

    public static DistanceStrategy distanceStrategy(int distance) {
        return Arrays.stream(values())
                .filter(s -> s.match.test(distance))
                .findAny()
                .map(s -> s.strategy)
                .orElse(UNDER_TEN.strategy);
    }
}

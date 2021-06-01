package wooteco.subway.path.domain.fare.distance;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceDiscountType {
    DEFAULT(new DefaultDistanceDiscountStrategy(), (distance) -> distance <= 10),
    MIDDLE(new MiddleDistanceDiscountStrategy(), (distance) -> distance > 10 && distance <= 50),
    FARTHEST(new FarthestDistanceDiscountStrategy(), (distance) -> distance > 50);

    private final DistanceStrategy strategy;
    private final Predicate<Integer> match;

    DistanceDiscountType(DistanceStrategy strategy, Predicate<Integer> match) {
        this.strategy = strategy;
        this.match = match;
    }

    public static DistanceStrategy strategy(int distance) {
        return Arrays.stream(values())
                .filter(s -> s.match.test(distance))
                .findAny()
                .map(s -> s.strategy)
                .orElse(DEFAULT.strategy);
    }
}

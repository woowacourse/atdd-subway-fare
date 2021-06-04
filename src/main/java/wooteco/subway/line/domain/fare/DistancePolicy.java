package wooteco.subway.line.domain.fare;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistancePolicy {
    DEFAULT(distance -> distance <= 10, distance -> 1250),
    TEN_TO_FIFTY(distance -> 10 < distance && distance <= 50, distance -> 1250 + (((distance - 11) / 5 + 1) * 100)),
    FIFTY_TO_END(distance -> 50 < distance, distance -> 2050 + (((distance - 51) / 8 + 1) * 100));

    private final Predicate<Integer> predicate;
    private final Function<Integer, Integer> function;

    DistancePolicy(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static int distanceFare(int distance) {
        return Arrays.stream(DistancePolicy.values())
                .filter(distancePolicy -> distancePolicy.predicate.test(distance))
                .findFirst()
                .orElse(DEFAULT)
                .function.apply(distance);
    }
}

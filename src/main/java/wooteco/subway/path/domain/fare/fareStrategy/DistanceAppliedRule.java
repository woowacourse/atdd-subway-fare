package wooteco.subway.path.domain.fare.fareStrategy;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

public enum DistanceAppliedRule {
    BASIC(distance -> (distance <= 10), BasicDistanceFare::new),
    EXTRA(distance -> (distance > 10) && (distance <= 40), ExtraDistanceFare::new),
    EXCESS(distance -> (distance>=40), ExcessDistanceFare::new);

    private IntPredicate distanceInterval;
    private Function<Integer, DistanceFare> distanceFare;

    DistanceAppliedRule(IntPredicate distanceInterval, Function<Integer, DistanceFare> distanceFare) {
        this.distanceInterval = distanceInterval;
        this.distanceFare = distanceFare;
    }

    public static long calculateDistanceFare(int distance) {
        return Arrays.stream(DistanceAppliedRule.values())
            .filter(element -> element.distanceInterval.test(distance))
            .findAny()
            .map(element-> element.distanceFare.apply(distance))
            .map(DistanceFare::value)
            .orElseThrow(() -> new IllegalArgumentException("해당 거리를 계산할 수 없습니다"));
    }
}

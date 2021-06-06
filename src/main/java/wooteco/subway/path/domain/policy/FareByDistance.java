package wooteco.subway.path.domain.policy;

import java.util.Arrays;
import java.util.function.Predicate;

import wooteco.subway.path.domain.policy.distance.DefaultDistancePolicy;
import wooteco.subway.path.domain.policy.distance.DistancePolicy;
import wooteco.subway.path.domain.policy.distance.FarDistancePolicy;
import wooteco.subway.path.domain.policy.distance.MiddleDistancePolicy;

public enum FareByDistance {
    DEFAULT_DISTANCE(new DefaultDistancePolicy(), (distance) -> distance <= 10),
    MIDDLE_DISTANCE(new MiddleDistancePolicy(), (distance) -> distance > 10 && distance <= 50),
    FAR_DISTANCE(new FarDistancePolicy(), (distance) -> distance > 50);

    private final DistancePolicy distancePolicy;
    private final Predicate<Integer> match;

    FareByDistance(DistancePolicy distancePolicy, Predicate<Integer> match) {
        this.distancePolicy = distancePolicy;
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
        return distancePolicy.calculate(distance);
    }
}

package wooteco.subway.path.domain.fare.distance;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum DistanceFarePolicy {
    DEFAULT(
        distance -> distance <= 10,
        DistanceFarePolicy::getDistanceDefaultFare
    ),
    OVER_TEN_LESS_THAN_FIFTY(
        distance -> 10 < distance && distance <= 50,
        DistanceFarePolicy::getDistanceFirstExtraFare
    ),
    OVER_FIFTY(
        distance -> 50 < distance,
        DistanceFarePolicy::getDistanceSecondExtraFare
    );

    private final IntPredicate distancePredicate;
    private final IntFunction<DistanceFare> distanceFareFunction;

    DistanceFarePolicy(IntPredicate distancePredicate, IntFunction<DistanceFare> distanceFareFunction) {
        this.distancePredicate = distancePredicate;
        this.distanceFareFunction = distanceFareFunction;
    }

    public static DistanceFare getDistanceFareByDistance(int distance) {
        DistanceFarePolicy distanceFarePolicy = getFarePolicyByDistance(distance);
        return distanceFarePolicy.getDistanceFare(distance);
    }

    private static DistanceFarePolicy getFarePolicyByDistance(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
            .filter(distanceFarePolicy -> distanceFarePolicy.distancePredicate.test(distance))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 거리에 해당하는 요금 정책이 없습니다."));
    }

    private static DistanceFare getDistanceDefaultFare(int distance) {
        return new DistanceDefaultFare(distance);
    }

    private static DistanceFare getDistanceFirstExtraFare(int distance) {
        return new DistanceFirstExtraFare(distance);
    }

    private static DistanceFare getDistanceSecondExtraFare(int distance) {
        return new DistanceSecondExtraFare(distance);
    }

    private DistanceFare getDistanceFare(int distance) {
        return distanceFareFunction.apply(distance);
    }
}
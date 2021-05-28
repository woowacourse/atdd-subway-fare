package wooteco.subway.fare.domain;

import wooteco.subway.exception.badrequest.fare.InvalidFareArgumentException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceFareStrategy implements FareStrategy {
    BASIC(distance -> distance <= 10) {
        @Override
        public int calculateFare(int distance) {
            return 1250;
        }
    },
    EXTRA_UNDER_FIFTY(distance -> 10 < distance && distance <= 50) {
        @Override
        public int calculateFare(int distance) {
            return BASIC.calculateFare(distance) + DistanceFareStrategy.calculateExtraFare(distance - 10, 5);
        }
    },
    EXTRA_OVER_FIFTY(distance -> distance > 50) {
        @Override
        public int calculateFare(int distance) {
            return EXTRA_UNDER_FIFTY.calculateFare(50) + DistanceFareStrategy.calculateExtraFare(distance - 50, 8);
        }
    };

    private final Predicate<Integer> policy;

    DistanceFareStrategy(Predicate<Integer> policy) {
        this.policy = policy;
    }

    private static int calculateExtraFare(int distance, int unitDistance) {
        return (int) Math.ceil(((double) (distance)) / unitDistance) * 100;
    }

    public static DistanceFareStrategy find(int distance) {
        return Arrays.stream(DistanceFareStrategy.values())
                .filter(distanceFareStrategy -> distanceFareStrategy.policy.test(distance))
                .findFirst()
                .orElseThrow(InvalidFareArgumentException::new);
    }
}

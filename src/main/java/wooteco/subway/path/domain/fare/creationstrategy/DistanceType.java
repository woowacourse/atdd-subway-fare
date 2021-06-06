package wooteco.subway.path.domain.fare.creationstrategy;

import java.util.Arrays;
import java.util.function.IntPredicate;
import wooteco.subway.exception.application.ValidationFailureException;

public enum DistanceType {

    BASIC_DISTANCE(
        distance -> distance <= 10
    ),
    SHORT_INTERVAL_DISTANCE(
        distance -> (10 < distance) && (distance <= 50)
    ),
    LONG_INTERVAL_DISTANCE(
        distance -> distance > 50
    );

    private final IntPredicate matchedFilter;

    DistanceType(IntPredicate matchedFilter) {
        this.matchedFilter = matchedFilter;
    }

    public static DistanceType from(int distance) {
        validatePositiveOrZero(distance);
        return Arrays.stream(values())
            .filter(rule -> rule.matchedFilter.test(distance))
            .findAny()
            .orElseThrow(() -> new ValidationFailureException("해당하는 거리가 존재하지 않습니다."));
    }

    private static void validatePositiveOrZero(int distance) {
        if (distance < 0) {
            throw new ValidationFailureException("거리는 음수일 수 없습니다.");
        }
    }
}

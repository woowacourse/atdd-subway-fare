package wooteco.subway.path.domain.fare.creationstrategy;

import static wooteco.subway.path.domain.fare.Fare.DEFAULT_FARE;
import static wooteco.subway.path.domain.fare.Fare.ZERO_FARE;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.path.domain.fare.Fare;

public class DistanceBasedCreationStrategy implements FareCreationStrategy {

    private static final int SHORT_INTERVAL_BOUNDARY = 50;
    private static final int EXCLUSION_FARE_DISTANCE = 10;
    private static final int SHORT_INTERVAL_UNIT = 5;
    private static final int LONG_INTERVAL_UNIT = 8;
    private static final int EXTRA_FARE_UNIT = 100;
    private static final Map<DistanceType, Function<Integer, Fare>> EXTRA_FARE_OPERATORS;

    static {
        EXTRA_FARE_OPERATORS = new HashMap<>();
        EXTRA_FARE_OPERATORS.put(
            DistanceType.BASIC_DISTANCE, distance -> ZERO_FARE
        );
        EXTRA_FARE_OPERATORS.put(
            DistanceType.SHORT_INTERVAL_DISTANCE, DistanceBasedCreationStrategy::calculateShortIntervalExtraFare
        );
        EXTRA_FARE_OPERATORS.put(
            DistanceType.LONG_INTERVAL_DISTANCE, DistanceBasedCreationStrategy::calculateBothIntervalExtraFare
        );
    }

    public DistanceBasedCreationStrategy() {
    }

    @Override
    public Fare generate(int distance) {
        DistanceType distanceType = DistanceType.from(distance);
        Fare extraFare = matchedExtraFareOperator(distanceType)
            .apply(distance);
        return DEFAULT_FARE.addFare(extraFare);
    }

    private Function<Integer, Fare> matchedExtraFareOperator(DistanceType distanceType) {
        if (!EXTRA_FARE_OPERATORS.containsKey(distanceType)) {
            throw new ValidationFailureException("거리별 요금 타입에 맞는 생성 함수를 찾지 못했습니다.");
        }
        return EXTRA_FARE_OPERATORS.get(distanceType);
    }

    private static Fare calculateBothIntervalExtraFare(int distance) {
        return calculateShortIntervalExtraFare(SHORT_INTERVAL_BOUNDARY)
            .addFare(
                calculateLongIntervalExtraFare(distance - SHORT_INTERVAL_BOUNDARY)
            );
    }

    private static Fare calculateShortIntervalExtraFare(int distance) {
        distance -= EXCLUSION_FARE_DISTANCE;
        int unitCount = (distance - 1) / SHORT_INTERVAL_UNIT + 1;
        return new Fare(unitCount * EXTRA_FARE_UNIT);
    }

    private static Fare calculateLongIntervalExtraFare(int distance) {
        int unitCount = (distance - 1) / LONG_INTERVAL_UNIT + 1;
        return new Fare(unitCount * EXTRA_FARE_UNIT);
    }
}

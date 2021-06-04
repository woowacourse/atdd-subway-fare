package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

public enum DistanceFareCalculator {
    DEFAULT_DISTANCE(0, 10, (distance) -> defaultFare()),
    OVER_10KM_TO_50KM(10, 50, DistanceFareCalculator::over10KmTo50Km),
    OVER_50KM(50, Integer.MAX_VALUE, DistanceFareCalculator::over50Km);

    private final int minDistance;
    private final int maxDistance;
    private final UnaryOperator<Integer> calculator;

    DistanceFareCalculator(int minDistance, int maxDistance, UnaryOperator<Integer> calculator) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.calculator = calculator;
    }

    public static int from(int distance) {
        return Arrays.stream(values())
                .filter(it -> (it.minDistance <= distance && distance < it.maxDistance))
                .map(it -> it.calculator.apply(distance))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("거리를 확인해주세요 해당하는 요금 구간을 찾을 수 없습니다."));
    }

    private static int defaultFare() {
        return 1250;
    }

    private static int over10KmTo50Km(int distance) {
        return defaultFare() + (int) ((Math.ceil(distance - 10 - 1) / 5 + 1)) * 100;
    }

    private static int over50Km(int distance) {
        return over10KmTo50Km(50) + (int) ((Math.ceil(distance - 50 - 1) / 8 + 1)) * 100;
    }
}

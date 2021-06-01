package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

public enum DistanceFareCalculator {
    DEFAULT_DISTANCE(0, 10, (distance) -> 1250),
    OVER_10KM_TO_50KM(10, 50,
            (distance) -> 1250 + (int) ((Math.ceil(distance - 10 - 1) / 5 + 1)) * 100),
    OVER_50KM(50, Integer.MAX_VALUE, (distance) ->
            1250 + (int) ((Math.ceil(50 - 10 - 1) / 5 + 1)) * 100
                    + (int) ((Math.ceil(distance - 50 - 1) / 8 + 1)) * 100);

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
                .orElseThrow(NoSuchElementException::new);
    }
}

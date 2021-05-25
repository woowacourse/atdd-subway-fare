package wooteco.subway.path.domain.farepolicy;

import java.math.BigDecimal;

public class FiftyOverPolicy implements FarePolicy {
    private static final int FARE = 100;
    private static final int OVER_FARE_DISTANCE = 8;
    private static final int THRESHOLD = 50;

    public BigDecimal calculate(int distance) {
        distance -= THRESHOLD;

        if (distance <= 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(caculateOverFare(distance));
    }

    private int caculateOverFare(int distance) {
        return (int) Math.ceil(
            (double) ((distance - 1) / OVER_FARE_DISTANCE) + 1
        ) * FARE;
    }
}

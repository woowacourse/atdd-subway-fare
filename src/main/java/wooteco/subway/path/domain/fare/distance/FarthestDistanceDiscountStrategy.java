package wooteco.subway.path.domain.fare.distance;

import static wooteco.subway.path.domain.fare.distance.MiddleDistanceDiscountStrategy.MAX_DISTANCE;

public class FarthestDistanceDiscountStrategy implements DistanceStrategy {
    private static final int UNIT = 8;
    private static final int UNIT_FARE = 100;

    @Override
    public int calculate(int distance) {
        MiddleDistanceDiscountStrategy strategy = new MiddleDistanceDiscountStrategy();
        int underFiftyFare = strategy.maxFare();
        distance -= MAX_DISTANCE;
        int count = (distance - 1) / UNIT;
        return underFiftyFare + (int) ((Math.ceil(count) + 1) * UNIT_FARE);
    }
}

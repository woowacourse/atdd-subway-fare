package wooteco.subway.path.domain.fare.distance;

import static wooteco.subway.path.domain.fare.distance.TenToFiftyStrategy.MAX_DISTANCE;

public class OverFiftyStrategy implements DistanceStrategy {
    private static final int UNIT = 8;
    private static final int UNIT_FARE = 100;

    @Override
    public int calculate(int distance) {
        TenToFiftyStrategy strategy = new TenToFiftyStrategy();
        int underFiftyFare = strategy.maxFare();
        distance -= MAX_DISTANCE;
        int count = (distance - 1) / UNIT;
        return underFiftyFare + (int) ((Math.ceil(count) + 1) * UNIT_FARE);
    }
}

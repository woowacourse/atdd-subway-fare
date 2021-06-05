package wooteco.subway.path.domain.strategy.discount;

public class DistanceAdditionFactory {
    private static final int BASIC_DISTANCE = 10;
    private static final int OVER_DISTANCE = 50;

    public static DistanceAdditionPolicy create(int distance, int defaultFare) {
        if (distance <= BASIC_DISTANCE) {
            return new UnderTenAddition(distance, defaultFare);
        }

        if (distance > OVER_DISTANCE) {
            return new OverFiftyAddition(distance, defaultFare);
        }

        return new TenToFiftyAddition(distance, defaultFare);
    }
}

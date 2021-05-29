package wooteco.subway.path.domain.fare.distance;

public class TenToFiftyStrategy implements DistanceStrategy {
    private static final int UNIT = 5;
    private static final int UNIT_FARE = 100;
    public static final int MIN_DISTANCE = 10;
    public static final int MAX_DISTANCE = 50;

    @Override
    public int calculate(int distance) {
        distance -= MIN_DISTANCE;
        return (int) ((Math.ceil((distance - 1) / UNIT) + 1) * UNIT_FARE);
    }

    public int maxFare() {
        return calculate(MAX_DISTANCE);
    }


}

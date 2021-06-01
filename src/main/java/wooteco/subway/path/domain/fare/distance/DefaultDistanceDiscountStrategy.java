package wooteco.subway.path.domain.fare.distance;

public class DefaultDistanceDiscountStrategy implements DistanceStrategy {
    @Override
    public int calculate(int distance) {
        return 0;
    }
}

package wooteco.subway.path.domain.fare.distance;

public class UnderTenStrategy implements DistanceStrategy {
    @Override
    public int calculate(int distance) {
        return 0;
    }
}

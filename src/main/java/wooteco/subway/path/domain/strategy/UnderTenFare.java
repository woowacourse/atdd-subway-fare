package wooteco.subway.path.domain.strategy;

public class UnderTenFare extends DistanceFarePolicy {
    public UnderTenFare(int distance, int defaultFare) {
        super(distance, defaultFare);
    }

    @Override
    protected int calculateAdditionalFare() {
        return 0;
    }
}

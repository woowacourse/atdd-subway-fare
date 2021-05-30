package wooteco.subway.path.domain.strategy.discount;

public class UnderTenAddition extends DistanceAdditionPolicy {
    public UnderTenAddition(int distance, int defaultFare) {
        super(distance, defaultFare);
    }

    @Override
    protected int calculateAdditionalFare() {
        return 0;
    }
}

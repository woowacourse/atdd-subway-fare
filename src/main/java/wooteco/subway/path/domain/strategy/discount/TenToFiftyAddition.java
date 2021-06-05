package wooteco.subway.path.domain.strategy.discount;

public class TenToFiftyAddition extends DistanceAdditionPolicy {
    public static final int DISTANCE_PIVOT = 10;
    public static final int CONDITION_PIVOT = 5;

    public TenToFiftyAddition(int distance, int defaultFare) {
        super(distance, defaultFare);
    }

    @Override
    protected int calculateAdditionalFare() {
        return (int) ((Math.ceil((distance() - DISTANCE_PIVOT) - 1) / CONDITION_PIVOT) + 1) * 100;
    }
}

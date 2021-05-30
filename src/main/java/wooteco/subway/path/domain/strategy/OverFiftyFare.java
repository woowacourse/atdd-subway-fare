package wooteco.subway.path.domain.strategy;

public class OverFiftyFare extends DistanceFarePolicy {
    public static final int DISTANCE_PIVOT = 50;
    public static final int CONDITION_PIVOT = 8;

    public OverFiftyFare(int distance, int defaultFare) {
        super(distance, defaultFare);
    }

    @Override
    protected int calculateAdditionalFare() {
        int underFiftyFare = underFiftyFare(defaultFare(), distance());
        return (int) ((Math.ceil((distance() - DISTANCE_PIVOT) - 1) / CONDITION_PIVOT) + 1) * 100 + underFiftyFare;
    }

    private int underFiftyFare(int defaultFare, int distance) {
        return new TenToFiftyFare(
                distance - ((distance % (DISTANCE_PIVOT * (distance / DISTANCE_PIVOT)))),
                defaultFare
        ).calculateAdditionalFare();
    }
}

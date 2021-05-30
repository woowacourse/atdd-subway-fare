package wooteco.subway.path.domain.strategy;

public abstract class DistanceFarePolicy {
    private final int distance;
    private final int defaultFare;

    protected DistanceFarePolicy(int distance, int defaultFare) {
        this.distance = distance;
        this.defaultFare = defaultFare;
    }

    public final int calculateFare(int extraFare) {
        return defaultFare + calculateAdditionalFare() + extraFare;
    }

    public int defaultFare() {
        return defaultFare;
    }

    public int distance() {
        return distance;
    }

    protected abstract int calculateAdditionalFare();
}

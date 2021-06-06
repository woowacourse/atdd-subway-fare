package wooteco.subway.path.domain.policy.distance;

public class DistancePolicy {
    private final int unitDistance;
    private final int extraDistance;
    private final int previousFare;

    public DistancePolicy(int unitDistance, int extraDistance, int previousFare) {
        this.unitDistance = unitDistance;
        this.extraDistance = extraDistance;
        this.previousFare = previousFare;
    }

    public int calculate(int distance) {
        return previousFare + (int)((Math.ceil((distance - extraDistance - 1) / unitDistance) + 1) * 100);
    }
}


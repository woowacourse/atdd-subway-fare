package wooteco.subway.path.domain.policy.age;

public abstract class AgePolicy {
    private final double rate;
    private final int deductionFare;

    public AgePolicy(double rate, int deductionFare) {
        this.rate = rate;
        this.deductionFare = deductionFare;
    }

    public int calculate(int fare) {
        return (int)((fare - deductionFare) * rate);
    }
}

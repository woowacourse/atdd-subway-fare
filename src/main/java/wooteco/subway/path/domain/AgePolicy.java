package wooteco.subway.path.domain;

public abstract class AgePolicy {
    private final double rate;
    private final int deductionFare;

    public AgePolicy(double rate, int deductionFare) {
        this.rate = rate;
        this.deductionFare = deductionFare;
    }

    public int calculate(int price) {
        return (int)((price - deductionFare) * rate);
    }
}

package wooteco.subway.path.domain.policy.age;

public class AdultPolicy extends AgePolicy {
    private static final double RATE = 1;
    private static final int DEDUCTION_FARE = 0;

    public AdultPolicy() {
        super(RATE, DEDUCTION_FARE);
    }
}

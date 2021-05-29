package wooteco.subway.path.domain.age;

public class BabyPolicy extends AgePolicy{
    private static final double RATE = 0;
    private static final int DEDUCTION_FARE = 0;

    public BabyPolicy() {
        super(RATE, DEDUCTION_FARE);
    }
}

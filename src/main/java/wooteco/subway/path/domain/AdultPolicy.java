package wooteco.subway.path.domain;

public class AdultPolicy extends AgePolicy {
    private static final double RATE = 100;
    private static final int DEDUCTION_FARE = 0;

    public AdultPolicy() {
        super(RATE, DEDUCTION_FARE);
    }
}

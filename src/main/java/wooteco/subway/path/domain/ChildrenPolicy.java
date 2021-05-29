package wooteco.subway.path.domain;

public class ChildrenPolicy extends AgePolicy {
    private static final double RATE = 0.5;
    private static final int DEDUCTION_FARE = 350;

    public ChildrenPolicy() {
        super(RATE, DEDUCTION_FARE);
    }
}

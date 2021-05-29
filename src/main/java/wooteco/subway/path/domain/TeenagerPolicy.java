package wooteco.subway.path.domain;

import wooteco.subway.path.domain.age.AgePolicy;

public class TeenagerPolicy extends AgePolicy {
    private static final double RATE = 0.8;
    private static final int DEDUCTION_FARE = 350;

    public TeenagerPolicy() {
        super(RATE, DEDUCTION_FARE);
    }
}

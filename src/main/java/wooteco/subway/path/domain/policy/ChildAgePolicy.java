package wooteco.subway.path.domain.policy;

public class ChildAgePolicy implements AgePolicy {

    private static final int DEDUCTION_FARE = 350;
    private static final double DISCOUNT_RATE = 0.5;
    private static final int MIN_AGE = 6;

    public ChildAgePolicy() {
    }

    @Override
    public void setNextPolicy(AgePolicy nextAgePolicy) {
    }

    @Override
    public int calculateFare(int age, int fare) {
        if (age < MIN_AGE) {
            return 0;
        }

        return (int) ((fare - DEDUCTION_FARE) * (1 - DISCOUNT_RATE));
    }
}

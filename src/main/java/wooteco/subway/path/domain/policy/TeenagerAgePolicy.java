package wooteco.subway.path.domain.policy;

public class TeenagerAgePolicy implements AgePolicy {

    private static final int DEDUCTION_FARE = 350;
    private static final double DISCOUNT_RATE = 0.2;
    private static final int MIN_AGE = 13;

    private AgePolicy nextAgePolicy;

    public TeenagerAgePolicy() {
    }

    @Override
    public void setNextPolicy(AgePolicy nextAgePolicy) {
        this.nextAgePolicy = nextAgePolicy;
    }

    @Override
    public int calculateFare(int age, int fare) {
        if (age < MIN_AGE) {
            return nextAgePolicy.calculateFare(age, fare);
        }

        return (int) ((fare - DEDUCTION_FARE) * (1 - DISCOUNT_RATE));
    }
}

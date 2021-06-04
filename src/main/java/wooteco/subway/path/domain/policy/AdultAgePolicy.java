package wooteco.subway.path.domain.policy;

public class AdultAgePolicy implements AgePolicy {

    private static final int MIN_AGE = 19;

    private AgePolicy nextAgePolicy;

    public AdultAgePolicy() {
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

        return fare;
    }
}

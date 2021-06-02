package wooteco.subway.path.domain;

public enum Age {

    ADULT(0),
    TEEN(0.2),
    CHILD(0.5),
    BABY(1);

    private static final int ADULT_AGE = 19;
    private static final int TEEN_AGE = 13;
    private static final int CHILD_AGE = 6;
    private static final int DISCOUNT_MONEY = 350;

    private double discountRate;

    Age(double discountRate) {
        this.discountRate = discountRate;
    }

    public static Age of(int age) {
        if(age >= ADULT_AGE) {
            return ADULT;
        }

        if(age >= TEEN_AGE) {
            return TEEN;
        }

        if(age >= CHILD_AGE) {
            return CHILD;
        }

        return BABY;
    }

    public int calculateFareByAge(int fare) {
        if(this.equals(BABY)) {
            return fare;
        }

        return (int) ((fare - DISCOUNT_MONEY) * this.discountRate);
    }
}

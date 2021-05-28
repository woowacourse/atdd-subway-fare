package wooteco.subway.path.domain;

public class FarePolicy {
    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_BOUND = 10;
    private static final int SECOND_BOUND = 50;
    private static final int ZERO = 0;
    private static final int EXTRA_FARE = 100;
    private static final double FIRST_DISTANCE_STANDARD = 5.0;
    private static final double SECOND_DISTANCE_STANDARD = 8.0;

    private final int fare;

    private FarePolicy(int fare) {
        this.fare = fare;
    }

    public static FarePolicy of(int distance) {
        return new FarePolicy(calculateBy(distance));
    }

    private static int calculateBy(int distance) {
        if (distance > SECOND_BOUND) {
            return DEFAULT_FARE + plusFirstBound(SECOND_BOUND) + plusSecondBound(distance);
        }
        return DEFAULT_FARE + plusFirstBound(distance);
    }

    private static int plusFirstBound(int distance) {
        if (distance < FIRST_BOUND) {
            return ZERO;
        }
        return (int) (Math.ceil((distance - FIRST_BOUND) / FIRST_DISTANCE_STANDARD) * EXTRA_FARE);
    }

    private static int plusSecondBound(int distance) {
        return (int) (Math.ceil((distance - SECOND_BOUND) / SECOND_DISTANCE_STANDARD) * EXTRA_FARE);
    }

    public int apply(DiscountPolicy discountPolicy) {
        return (int) ((fare - discountPolicy.staticDiscount()) * discountPolicy.discountRate());
    }
}

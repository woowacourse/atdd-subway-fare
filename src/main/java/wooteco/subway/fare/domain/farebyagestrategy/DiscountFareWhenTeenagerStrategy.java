package wooteco.subway.fare.domain.farebyagestrategy;

public class DiscountFareWhenTeenagerStrategy implements DiscountFareByAgeStrategy {

    private static final int TEENAGER_AGE_LOWER_BOUNDARY = 13;
    private static final int TEENAGER_AGE_UPPER_BOUNDARY = 18;
    private static final int DISCOUNT_FOR_TEENAGER = 350;
    private static final double DISCOUNT_RATE_FOR_TEENAGER = 0.8;

    @Override
    public boolean isInAgeRange(int age) {
        return TEENAGER_AGE_LOWER_BOUNDARY <= age & age <= TEENAGER_AGE_UPPER_BOUNDARY;
    }

    @Override
    public int discountFareByAge(int fare) {
        return (int) ((fare - DISCOUNT_FOR_TEENAGER) * DISCOUNT_RATE_FOR_TEENAGER);
    }
}

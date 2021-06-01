package wooteco.subway.member.domain.agemember;

public class Child implements AgeMember {

    private static final int CHILD_AGE_LOWER_BOUNDARY = 6;
    private static final int CHILD_AGE_UPPER_BOUNDARY = 12;
    private static final double DISCOUNT_RATE_FOR_CHILD = 0.5;
    private static final int DISCOUNT_PRICE_FOR_CHILD = 350;

    @Override
    public boolean isInAgeRange(int age) {
        return CHILD_AGE_LOWER_BOUNDARY <= age & age <= CHILD_AGE_UPPER_BOUNDARY;
    }

    @Override
    public int discountFareByAge(int fare) {
        return (int) ((fare - DISCOUNT_PRICE_FOR_CHILD) * DISCOUNT_RATE_FOR_CHILD);
    }
}

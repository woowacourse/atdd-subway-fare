package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;

public class Fare {

    private static final int BASIC_DISTANCE = 10;
    private static final int EXCEED_INSTANCE = 40;
    private static final int BASIC_FARE = 1250;
    private static final int EXTRA_FIRST_DISTANCE_UNIT = 5;
    private static final int EXTRA_SECOND_DISTANCE_UNIT = 8;
    private static final int EXTRA_FARE_UNIT = 100;
    private static final int EXTRA_FIRST_FULL_FARE = 800;

    private final int value;

    public Fare(int distance, LoginMember member, int extraFare) {
        this.value = calculateFare(distance, member, extraFare);
    }

    private int calculateFare(int distance, LoginMember member, int extraFare) {
        int basicFare = calculateFareByDistance(distance) + extraFare;
        AgeAppliedRule ageAppliedRule = AgeAppliedRule.matchRule(member.getAge());
        return (int) ((basicFare - ageAppliedRule.getDeductedFare()) * ageAppliedRule.getAppliedRate());
    }

    private int calculateFareByDistance(int distance) {
        if (distance < BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        return BASIC_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if (distance - BASIC_DISTANCE - EXCEED_INSTANCE < 0) {
            return (int) (
                (Math.ceil((distance - BASIC_DISTANCE - 1) / EXTRA_FIRST_DISTANCE_UNIT) + 1)
                    * EXTRA_FARE_UNIT);
        }
        return (int) ((Math.ceil((distance - BASIC_DISTANCE - EXCEED_INSTANCE - 1) / EXTRA_SECOND_DISTANCE_UNIT) + 1) * EXTRA_FARE_UNIT) + EXTRA_FIRST_FULL_FARE;
    }

    public int value() {
        return this.value;
    }
}

package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.exception.FareCalculateException;
import wooteco.subway.member.domain.LoginMember;

public class FareCalculator {

    private static final int DEFAULT_FARE = 1250;
    private static final double TEENAGER_DISCOUNT_PERCENT = 0.2;
    private static final double CHILD_DISCOUNT_PERCENT = 0.5;
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 13;
    private static final int TEENAGER_MIN_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;
    private static final int DISCOUNT_EXCLUDE_FARE = 350;
    private static final int OVERFARE_DISTANCE_10KM_STANDARD = 10;
    private static final int OVERFARE_DISTANCE_50KM_STANDARD = 50;
    private static final int MONEY_UNIT = 100;
    private static final int OVERFARE_DISTANCE_10KM_PER_UNIT = 5;
    private static final int OVERFARE_DISTANCE_50KM_PER_UNIT = 8;
    private static final int DEFAULT_OVERFARE_IN_50KM_DISTANCE = 800;

    public int calculate(LoginMember member, int distance, List<SectionEdge> sectionEdges) {
        int fare = DEFAULT_FARE + calculateOverFare(distance, sectionEdges);
        return discountFare(member, fare);
    }

    private int discountFare(LoginMember member, int fare) {
        if (member.getId() == null) {
            return fare;
        }
        if (isTeenager(member)) {
            return (int) (fare - ((fare - DISCOUNT_EXCLUDE_FARE) * TEENAGER_DISCOUNT_PERCENT));
        }
        if (isChild(member)) {
            return (int) (fare - ((fare - DISCOUNT_EXCLUDE_FARE) * CHILD_DISCOUNT_PERCENT));
        }
        return fare;
    }

    private boolean isChild(LoginMember member) {
        return member.getAge() >= CHILD_MIN_AGE && member.getAge() < CHILD_MAX_AGE;
    }

    private boolean isTeenager(LoginMember member) {
        return member.getAge() >= TEENAGER_MIN_AGE && member.getAge() < TEENAGER_MAX_AGE;
    }

    private int calculateOverFare(int distance, List<SectionEdge> sectionEdges) {
        return calculateDistanceOverFare(distance) + calculateLineOverFare(sectionEdges);
    }

    private int calculateLineOverFare(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
            .mapToInt(edges -> edges.getLine().getFare())
            .max()
            .orElseThrow(FareCalculateException::new)
            ;
    }

    private int calculateDistanceOverFare(int distance) {
        if (distance > OVERFARE_DISTANCE_10KM_STANDARD && distance <= 50) {
            return (int) ((Math.ceil(
                (distance - OVERFARE_DISTANCE_10KM_STANDARD) / OVERFARE_DISTANCE_10KM_PER_UNIT) + 1)
                * MONEY_UNIT);
        }
        if (distance > OVERFARE_DISTANCE_50KM_STANDARD) {
            return DEFAULT_OVERFARE_IN_50KM_DISTANCE + (int) ((Math.ceil(
                (distance - OVERFARE_DISTANCE_50KM_STANDARD) / OVERFARE_DISTANCE_50KM_PER_UNIT) + 1)
                * MONEY_UNIT);
        }
        return 0;
    }
}

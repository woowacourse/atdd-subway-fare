package wooteco.subway.path.domain;

import java.util.List;
import org.springframework.stereotype.Component;
import wooteco.subway.path.AgeSet;

@Component
public class FareCalculator {

    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_CHARGE = 100;
    private static final int TEN_TO_FIFTY_EXTRA_CHARGE_DISTANCE = 5;
    private static final int OVER_FIFTY_EXTRA_CHARGE_DISTANCE = 8;
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int FIFTY_DISTANCE = 50;


    private FareCalculator() {}

    public static int calculateFare(int distance, int age, List<SectionEdge> sectionEdges) {
        int fare = calculateOverFare(distance);
        fare += getExpensiveLineExtraFare(sectionEdges);
        fare -= getAgeDisCount(fare, age);
        return fare;
    }

    private static int calculateOverFare(int distance) {
        return DEFAULT_FARE + distanceTenToFifty(distance) + distanceMoreThanFifty(distance);
    }

    private static int distanceTenToFifty(int distance) {
        distance = Math.min(distance, FIFTY_DISTANCE) - DEFAULT_FARE_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / TEN_TO_FIFTY_EXTRA_CHARGE_DISTANCE) + 1)
            * EXTRA_CHARGE);
    }

    private static int distanceMoreThanFifty(int distance) {
        distance -= FIFTY_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / OVER_FIFTY_EXTRA_CHARGE_DISTANCE) + 1)
            * EXTRA_CHARGE);
    }

    private static int getExpensiveLineExtraFare(List<SectionEdge> sectionEdges) {
        int expensiveFare = 0;
        for (SectionEdge sectionEdge : sectionEdges) {
            expensiveFare = Math.max(sectionEdge.getExtraFare(), expensiveFare);
        }
        return expensiveFare;
    }

    private static int getAgeDisCount(int fare, int age) {
        AgeSet userAge = AgeSet.of(age);
        return userAge.ageDisCount(fare);
    }
}

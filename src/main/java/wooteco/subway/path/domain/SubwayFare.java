package wooteco.subway.path.domain;

import java.util.List;

public class SubwayFare {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXTRA_DISTANCE = 50;
    private static final int ADULT_AGE = 19;
    private static final int TEEN_AGE = 13;
    private static final int CHILD_AGE = 6;
    private static final int DISCOUNT_MONEY = 350;
    private static final double DISCOUNT_RATE_TEEN_AGE = 0.2;
    private static final double DISCOUNT_RATE_CHILD_AGE = 0.5;

    private List<SectionEdge> sectionEdges;

    public SubwayFare(List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    public int calculateFare(int distance, int age) {
        int fareByDistance = calculateFareByDistance(distance);

        return fareByDistance - calculateFareByAge(fareByDistance, age);
    }

    private int calculateFareByDistance(int distance) {
        return DEFAULT_FARE + distanceTenToFifty(distance) + distanceMoreThanFifty(distance) + getExpensiveFare();
    }

    private int calculateFareByAge(int fare, int age) {

        if (age >= ADULT_AGE) {
            return 0;
        }

        if (age >= TEEN_AGE) {
            return (int) ((fare - DISCOUNT_MONEY) * DISCOUNT_RATE_TEEN_AGE);
        }

        if (age >= CHILD_AGE) {
            return (int) ((fare - DISCOUNT_MONEY) * DISCOUNT_RATE_CHILD_AGE);
        }

        return fare;
    }

    private int getExpensiveFare() {
        int fare = 0;

        for(SectionEdge sectionEdge : sectionEdges) {
            int lineFare = sectionEdge.getFare();

            fare = Math.max(fare, lineFare);
        }

        return fare;
    }

    private int distanceTenToFifty(int distance) {
        distance = Math.min(distance, EXTRA_DISTANCE) - DEFAULT_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int distanceMoreThanFifty(int distance) {
        distance -= EXTRA_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

}

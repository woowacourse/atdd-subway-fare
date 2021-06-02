package wooteco.subway.path.domain;

import java.util.List;

public class SubwayFare {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXTRA_DISTANCE = 50;

    private final List<SectionEdge> sectionEdges;

    public SubwayFare(List<SectionEdge> sectionEdges) {
        this.sectionEdges = sectionEdges;
    }

    public int calculateFare(int distance, int age) {
        int fareByDistance = calculateFareByDistance(distance);

        return fareByDistance - getDiscountMoney(fareByDistance, age);
    }

    private int calculateFareByDistance(int distance) {
        DistanceFare distanceFare = DistanceFare.of(distance);
        return distanceFare.calculateFareByDistance(distance) + getExpensiveFare();
    }

    private int getExpensiveFare() {
        int fare = 0;

        for(SectionEdge sectionEdge : sectionEdges) {
            int lineFare = sectionEdge.getFare();

            fare = Math.max(fare, lineFare);
        }

        return fare;
    }

    private int getDiscountMoney(int fare, int age) {
        Age discountAge = Age.of(age);
        return discountAge.calculateFareByAge(fare);
    }
}

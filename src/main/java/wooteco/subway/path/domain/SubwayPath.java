package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.member.domain.Age;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_CHARGE = 100;
    private static final int TEN_TO_FIFTY_EXTRA_CHARGE_DISTANCE = 5;
    private static final int OVER_FIFTY_EXTRA_CHARGE_DISTANCE = 8;
    private static final int DEFAULT_FARE_DISTANCE = 10;
    private static final int FIFTY_DISTANCE = 50;
    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double TEENAGER_DISCOUNT = 0.2;
    private static final double CHILDREN_DISCOUNT = 0.5;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateFare(int distance, int age) {
        int fare = calculateOverFare(distance);
        fare += getExpensiveLineExtraFare();
        fare -= getAgeDisCount(fare, age);
        return fare;
    }

    private int calculateOverFare(int distance) {
        return DEFAULT_FARE + distanceTenToFifty(distance) + distanceMoreThanFifty(distance);
    }

    private int distanceTenToFifty(int distance) {
        distance = Math.min(distance, FIFTY_DISTANCE) - DEFAULT_FARE_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / TEN_TO_FIFTY_EXTRA_CHARGE_DISTANCE) + 1) * EXTRA_CHARGE);
    }

    private int distanceMoreThanFifty(int distance) {
        distance -= FIFTY_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / OVER_FIFTY_EXTRA_CHARGE_DISTANCE) + 1) * EXTRA_CHARGE);
    }

    private int getExpensiveLineExtraFare() {
        int expensiveFare = 0;
        for (SectionEdge sectionEdge : sectionEdges) {
            expensiveFare = Math.max(sectionEdge.getExtraFare(), expensiveFare);
        }
        return expensiveFare;
    }

    private int getAgeDisCount(int fare, int age) {
        Age userAge = new Age(age);
        if (userAge.isAdult()) {
            return 0;
        }
        if (userAge.isTeenager()) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * TEENAGER_DISCOUNT);
        }
        if (userAge.isChild()) {
            return (int) ((fare - DEDUCTIBLE_AMOUNT) * CHILDREN_DISCOUNT);
        }
        return fare;
    }
}

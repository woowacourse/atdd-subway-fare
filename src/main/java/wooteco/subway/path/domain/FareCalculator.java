package wooteco.subway.path.domain;

import java.util.List;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;

    private final List<SectionEdge> sectionEdges;
    private final int totalDistance;
    private final int passengerAge;

    public FareCalculator(List<SectionEdge> sectionEdges, int totalDistance, int passengerAge) {
        this.sectionEdges = sectionEdges;
        this.totalDistance = totalDistance;
        this.passengerAge = passengerAge;
    }

    public int fare() {
        int fare = BASIC_FARE;

        fare = DistanceFarePolicy.distancePolicyAppliedFare(fare, totalDistance);
        fare += lineExtraFare();
        fare = AgeFarePolicy.agePolicyAppliedFare(fare, passengerAge);

        return fare;
    }

    private int lineExtraFare() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::lineExtraFare)
            .max()
            .orElse(0);
    }

}

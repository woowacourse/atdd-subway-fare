package wooteco.subway.path.domain;

import java.util.List;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    public Fare() {
    }

    public int calculateByDistanceAndLine(int distance, List<SectionEdge> sectionEdges) {
        int fare = calculateByDistance(distance);
        fare += calculateAdditionalFareByLine(sectionEdges);
        return fare;
    }

    private int calculateByDistance(int distance) {
        FarePolicy bound = FarePolicy.findBound(distance);
        int overFare = calculateOverFare(distance, bound);
        return DEFAULT_FARE + overFare;
    }

    private int calculateOverFare(int distance, FarePolicy bound) {
        return (int) (Math.ceil((distance - 10) / bound.getKilometer()) * bound.getExtraFare());
    }

    private int calculateAdditionalFareByLine(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(0);
    }
}

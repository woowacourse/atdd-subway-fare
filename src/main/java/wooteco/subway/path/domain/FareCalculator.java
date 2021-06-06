package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.path.AgeSet;
import wooteco.subway.path.DistanceSet;

public class FareCalculator {

    private FareCalculator() {}

    public static int calculateFare(int distance, AgeSet ageSet, List<SectionEdge> sectionEdges) {

        DistanceSet pathDistance = DistanceSet.of(distance);
        int fare = pathDistance.calculateDistanceFare(distance);
        fare += getExpensiveLineExtraFare(sectionEdges);
        return fare - ageSet.ageDisCount(fare);
    }

    private static int getExpensiveLineExtraFare(List<SectionEdge> sectionEdges) {
        int expensiveFare = 0;
        for (SectionEdge sectionEdge : sectionEdges) {
            expensiveFare = Math.max(sectionEdge.getExtraFare(), expensiveFare);
        }
        return expensiveFare;
    }
}

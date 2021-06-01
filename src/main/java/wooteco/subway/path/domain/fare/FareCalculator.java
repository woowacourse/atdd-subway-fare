package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;

import java.util.List;

public class FareCalculator {
    private static final int ZERO = 0;

    public static int calculateFare(final SubwayPath subwayPath, final Integer age) {
        int fareByDistance = getExtraFareByDistance(subwayPath.getDistance(), getMaximumExtraFareByLine(subwayPath.getSectionEdges()));
        return getDiscountedFareByAge(fareByDistance, age);
    }

    private static int getMaximumExtraFareByLine(final List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(ZERO);
    }

    private static int getExtraFareByDistance(final int distance, final int fare) {
        return DistancePolicy.getFareApplyPolicy(distance, fare);
    }

    private static int getDiscountedFareByAge(final int fare, final int age) {
        return AgePolicy.getFareApplyPolicy(fare, age);
    }
}

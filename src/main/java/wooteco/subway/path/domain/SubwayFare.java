package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.path.exception.PathException;

import java.util.List;

public class SubwayFare {

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
        return sectionEdges.stream()
                .mapToInt(SectionEdge::getFare)
                .max()
                .orElseThrow(() -> new SubwayCustomException(PathException.INVALID_DISTANCE_EXCEPTION));
    }

    private int getDiscountMoney(int fare, int age) {
        Age discountAge = Age.of(age);
        return discountAge.calculateFareByAge(fare);
    }
}

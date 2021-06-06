package wooteco.subway.path.domain;

import java.util.List;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.policy.FareByAge;
import wooteco.subway.path.domain.policy.FareByDistance;
import wooteco.subway.path.infrastructure.SectionEdge;
import wooteco.subway.path.infrastructure.SubwayPath;

public class Fare {
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public Fare(SubwayPath subwayPath, int age) {
        this.fare = calculate(subwayPath, age);
    }

    private int calculate(SubwayPath subwayPath, int age) {
        int fareByDistance =
            FareByDistance.calculate(subwayPath.getDistance()) + findMaxExtraFare(subwayPath.getSectionEdges());
        return FareByAge.calculate(fareByDistance, age);
    }

    private int findMaxExtraFare(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
    }

    public int getFare() {
        return fare;
    }
}

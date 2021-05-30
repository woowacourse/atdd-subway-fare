package wooteco.subway.path.domain;

import java.util.List;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.policy.FareByDistance;
import wooteco.subway.station.domain.Station;

public class SubwayPath {
    private List<SectionEdge> sectionEdges;
    private List<Station> stations;
    private int distance;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, int distance) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.distance = distance;
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int calculateFareByDistance() {
        this.distance = sectionEdges.stream()
            .mapToInt(it -> it.getSection().getDistance())
            .sum();
        return FareByDistance.calculate(distance) + findMaxExtraFare();
    }

    private int findMaxExtraFare() {
        return this.sectionEdges.stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
    }
}

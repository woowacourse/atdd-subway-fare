package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private final List<Station> stations;
    private final int distance;
    private final int fare;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.stations = stations;
        this.distance = sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
        this.fare = FarePolicy.calculate(distance);
    }

    public List<Station> stations() {
        return stations;
    }

    public int distance() {
        return distance;
    }

    public int fare() {
        return fare;
    }
}

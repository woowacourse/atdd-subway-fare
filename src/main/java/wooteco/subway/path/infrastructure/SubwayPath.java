package wooteco.subway.path.infrastructure;

import java.util.List;

import wooteco.subway.station.domain.Station;

public class SubwayPath {
    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private final int distance;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.distance = sectionEdges.stream()
            .mapToInt(it -> it.getSection().getDistance())
            .sum();
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
}

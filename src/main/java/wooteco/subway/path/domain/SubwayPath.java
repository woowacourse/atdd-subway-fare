package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getSectionDistance)
            .sum();
    }

    public int maximumExtraFare() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getLineExtraFare)
            .max()
            .orElse(0);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }
}

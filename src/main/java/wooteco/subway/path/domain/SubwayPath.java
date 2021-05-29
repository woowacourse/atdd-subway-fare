package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(SectionEdge::distance).sum();
    }

    public int extraFare() {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max().orElse(0);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }
}

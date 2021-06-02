package wooteco.subway.path.domain;

import wooteco.subway.exception.path.InvalidPathException;
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

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateLineFare() {
        return sectionEdges.stream()
                .map(SectionEdge::getLine)
                .distinct()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(InvalidPathException::new);
    }
}

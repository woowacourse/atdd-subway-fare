package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

public class SubwayPath {
    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private final Set<Line> lines;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, Set<Line> lines) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.lines = lines;
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

    public int getMaxLineFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}

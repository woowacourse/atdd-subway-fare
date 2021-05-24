package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

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
        return sectionEdges.stream()
            .mapToInt(it -> it.getSection().getDistance())
            .sum();
    }

    public int findMaxExtraPrice(){
        return this.sectionEdges.stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
    }
}

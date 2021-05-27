package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {

    private final List<Station> stations;
    private final SectionEdges sectionEdges;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = new SectionEdges(sectionEdges);
        this.stations = stations;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Line> getLines() {
        return sectionEdges.getLines();
    }

    public int calculateDistance() {
        return sectionEdges.calculateDistance();
    }

}

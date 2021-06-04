package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.section.domain.Distance;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.domain.Stations;

public class SubwayPath {

    private final SectionEdges sectionEdges;
    private final Stations stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = new SectionEdges(sectionEdges);
        this.stations = new Stations(stations);
    }

    public Lines getLines() {
        return new Lines(sectionEdges.getLines());
    }

    public Stations getStations() {
        return stations;
    }

    public Distance calculateDistance() {
        return new Distance(sectionEdges.calculateDistance());
    }
}

package wooteco.subway.path.domain;

import java.math.BigDecimal;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private final Fare fare;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, Fare fare) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.fare = fare;
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

    public BigDecimal fare() {
        return fare.calculate(this);
    }

}

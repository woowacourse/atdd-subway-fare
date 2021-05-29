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

    public List<Station> getStations() {
        return stations;
    }

    public int distance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int subwayFare(int age) {
        FareCalculator fareCalculator = new FareCalculator(sectionEdges, distance(), age);
        return fareCalculator.fare();
    }

}

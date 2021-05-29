package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

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

    public int subwayFare(int age){
        FareCalculator fareCalculator = new FareCalculator(sectionEdges, distance(), age);
        return fareCalculator.fare();
    }

}

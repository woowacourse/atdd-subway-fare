package wooteco.subway.path.domain;

import wooteco.subway.path.application.InvalidPathException;
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
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateFare() {
        int totalDistance = calculateDistance();
        FarePolicy farePolicy = FarePolicy.of(totalDistance);

        int fare = farePolicy.calculateFare(totalDistance);
        fare += calculateExtraFare();

        return fare;
    }

    private int calculateExtraFare() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getLineExtraFare)
            .max()
            .orElseThrow(InvalidPathException::new);
    }
}

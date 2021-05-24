package wooteco.subway.path.domain;

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
        int fare = 1250;

        if (totalDistance > 10 && totalDistance <= 50) {
            fare += calculateOverFare(totalDistance - 10, 5);
        } else if (totalDistance > 50) {
            fare += calculateOverFare(40, 5);
            fare += calculateOverFare(totalDistance - 50, 8);
        }

        return fare;
    }

    private int calculateOverFare(int distance, int km) {
        return (int) ((Math.ceil((distance - 1) / km) + 1)  * 100);
    }
}

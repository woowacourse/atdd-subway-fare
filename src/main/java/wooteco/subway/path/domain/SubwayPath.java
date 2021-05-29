package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.station.domain.Station;

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
        int distance = calculateDistance();
        int fare = 1_250;

        fare += calculateAdditionalFareByDistance(distance);
        fare += calculateAdditionalLineFare();

        return fare;
    }

    private int calculateAdditionalLineFare() {
        return sectionEdges
            .stream()
            .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
            .max()
            .orElse(0);
    }

    private int calculateAdditionalFareByDistance(int distance) {
        int fare = 0;

        if (distance > 10 && distance <= 50) {
            fare += calculateAdditionalFareByDistanceUnderFifty(distance);
        }

        if (distance > 50) {
            fare += calculateAdditionalFareByDistanceOverFifty(distance);
        }
        return fare;
    }

    private int calculateAdditionalFareByDistanceUnderFifty(int distance) {
        return calculateOverFareByKM(distance - 10, 5);
    }

    private int calculateAdditionalFareByDistanceOverFifty(int distance) {
        return calculateAdditionalFareByDistanceUnderFifty(distance - (distance - 50)) + calculateOverFareByKM(distance - 50, 8);
    }

    private int calculateOverFareByKM(int distance, int km) {
        return (int) ((Math.ceil((distance - 1) / km) + 1) * 100);
    }
}

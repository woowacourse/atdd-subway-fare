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
        fare += calculateAdditionalFareByLine();

        return fare;
    }

    private int calculateAdditionalFareByDistance(int distance) {
        if (distance <= 10) {
            return 0;
        }
        if (distance <= 50) {
            return calculateAdditionalFareByDistanceUnderFifty(distance);
        }
        return calculateAdditionalFareByDistanceOverFifty(distance);
    }

    private int calculateAdditionalFareByDistanceUnderFifty(int distance) {
        return calculateOverFare(distance - 10, 5);
    }

    private int calculateAdditionalFareByDistanceOverFifty(int distance) {
        return calculateAdditionalFareByDistanceUnderFifty(distance - (distance - 50)) + calculateOverFare(distance - 50, 8);
    }

    private int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * 100);
    }

    private int calculateAdditionalFareByLine() {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(0);
    }
}

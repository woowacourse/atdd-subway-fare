package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
    private static final int DEFAULT_FARE = 1250;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private Integer distance;

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
        if (distance != null) {
            return distance;
        }

        this.distance = sectionEdges.stream()
                .mapToInt(it -> it.getSection().getDistance())
                .sum();
        return distance;
    }

    public int calculateFare() {
        int distance = calculateDistance();

        return distanceAdditionalFare(distance)
                + lineAdditionalFare();
    }

    private int distanceAdditionalFare(int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }

        if (distance <= OVER_LIMIT_DISTANCE) {
            int additionalFare = calculateAdditionalFareOver10km(distance);
            return DEFAULT_FARE + additionalFare;
        }

        int additionalFareOver10km = calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE);
        int additionalFareOver50km = calculateAdditionalFareOver50km(distance);

        return DEFAULT_FARE
                + additionalFareOver10km
                + additionalFareOver50km;
    }

    private int lineAdditionalFare() {
        return sectionEdges.stream()
                .map(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max(Integer::compare)
                .orElse(0);
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - DEFAULT_DISTANCE - 1) / 5) + 1) * 100);
    }

    private int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - OVER_LIMIT_DISTANCE - 1) / 8) + 1) * 100);
    }
}

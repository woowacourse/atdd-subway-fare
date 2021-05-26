package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    public static final int DEFAULT_FARE = 1250;
    public static final int DEFAULT_DISTANCE_PIVOT = 10;
    public static final int DEFAULT_CONDITION_PIVOT = 5;
    public static final int LONG_DISTANCE_PIVOT = 50;
    public static final int LONG_CONDITION_PIVOT = 8;
    public static final int ADDITIONAL_FARE = 100;


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
        int extraFare = findMaximumExtraFare();

        int distance = calculateDistance();
        if (distance <= DEFAULT_DISTANCE_PIVOT) {
            return DEFAULT_FARE + extraFare;
        }

        if (distance > LONG_DISTANCE_PIVOT) {
            return DEFAULT_FARE + calculateAdditionalFare(distance - (distance % LONG_DISTANCE_PIVOT) - DEFAULT_DISTANCE_PIVOT, DEFAULT_CONDITION_PIVOT) + calculateAdditionalFare(distance - LONG_DISTANCE_PIVOT, LONG_CONDITION_PIVOT) + extraFare;
        }

        return DEFAULT_FARE + calculateAdditionalFare(distance - DEFAULT_DISTANCE_PIVOT, DEFAULT_CONDITION_PIVOT) + extraFare;
    }

    private int findMaximumExtraFare() {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(0);
    }

    private int calculateAdditionalFare(int distance, int condition) {
        return (int) ((Math.ceil((distance - 1) / condition) + 1) * ADDITIONAL_FARE);
    }
}

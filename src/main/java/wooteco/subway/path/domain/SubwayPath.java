package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
    private static final int NO_ADDITIONAL_FARE = 0;
    private final int DEFAULT_FARE = 1250;

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

    public int calculateFare(int distance) {
        int fare = DEFAULT_FARE;
        fare += calculateAdditionalFareByDistance(distance);
        fare += calculateAdditionalFareByLine();

        return fare;
    }

    private int calculateAdditionalFareByDistance(int distance) {
        if (DEFAULT_DISTANCE < distance & distance <= OVER_LIMIT_DISTANCE) {
            return calculateAdditionalFareOver10km(distance - DEFAULT_DISTANCE);
        }

        if (OVER_LIMIT_DISTANCE < distance) {
            return calculateAdditionalFareOver50km(distance - OVER_LIMIT_DISTANCE);
        }
        return NO_ADDITIONAL_FARE;
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private int calculateAdditionalFareByLine() {
        return sectionEdges.stream()
            .mapToInt(section -> section.getExtraFare())
            .max()
            .getAsInt();
    }
}

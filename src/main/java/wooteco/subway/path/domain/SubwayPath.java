package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
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

    public int calculateFare(Integer age, int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return discountByAge(age, DEFAULT_FARE);
        }

        if (distance <= OVER_LIMIT_DISTANCE) {
            int overFare = calculateAdditionalFareOver10km(distance - DEFAULT_DISTANCE);
            return discountByAge(age,DEFAULT_FARE + overFare);
        }

        int additionalFareOver10km = calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE - DEFAULT_DISTANCE);
        int additionalFareOver50km = calculateAdditionalFareOver50km(distance - OVER_LIMIT_DISTANCE);

        return discountByAge(age, DEFAULT_FARE
                + additionalFareOver10km
                + additionalFareOver50km);
    }

    private int discountByAge(Integer age, int fare) {
        if (age < 6) {
            return 0;
        }
        if (age < 13) {
            return (int) ((fare-350)*0.5);
        }

        if( age < 19) {
            return (int) ((fare-350)*0.8);
        }
        return fare;
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}

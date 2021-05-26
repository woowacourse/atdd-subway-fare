package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

public class SubwayPath {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
    private final int DEFAULT_FARE = 1250;

    private List<SectionEdge> sectionEdges;
    private List<Station> stations;
    private Set<Line> lines;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, Set<Line> lines) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.lines = lines;
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
        int maxExtraFare = findExtraFare();

        if (distance <= DEFAULT_DISTANCE) {
            return discountByAge(age, DEFAULT_FARE + maxExtraFare);
        }

        if (distance <= OVER_LIMIT_DISTANCE) {
            int overFare = calculateAdditionalFareOver10km(distance - DEFAULT_DISTANCE);
            return discountByAge(age,DEFAULT_FARE + overFare + maxExtraFare);
        }

        int additionalFareOver10km = calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE - DEFAULT_DISTANCE);
        int additionalFareOver50km = calculateAdditionalFareOver50km(distance - OVER_LIMIT_DISTANCE);

        return discountByAge(age, DEFAULT_FARE + maxExtraFare
                + additionalFareOver10km
                + additionalFareOver50km);
    }

    private int findExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
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

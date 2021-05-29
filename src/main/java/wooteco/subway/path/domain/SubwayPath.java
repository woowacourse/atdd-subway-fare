package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.strategy.FareStrategy;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

public class SubwayPath {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
    private final int DEFAULT_FARE = 1250;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private final Set<Line> lines;
    private final FareStrategy fareStrategy;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, Set<Line> lines, FareStrategy fareStrategy) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.lines = lines;
        this.fareStrategy = fareStrategy;
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
        int maxExtraFare = findExtraFare();

        if (distance <= DEFAULT_DISTANCE) {
            return fareStrategy.discount(DEFAULT_FARE + maxExtraFare);
        }

        if (distance <= OVER_LIMIT_DISTANCE) {
            int overFare = calculateAdditionalFareOver10km(distance - DEFAULT_DISTANCE);
            return fareStrategy.discount(DEFAULT_FARE + overFare + maxExtraFare);
        }

        int additionalFareOver10km = calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE - DEFAULT_DISTANCE);
        int additionalFareOver50km = calculateAdditionalFareOver50km(distance - OVER_LIMIT_DISTANCE);

        return fareStrategy.discount(DEFAULT_FARE + maxExtraFare
                + additionalFareOver10km
                + additionalFareOver50km);
    }

    private int findExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}

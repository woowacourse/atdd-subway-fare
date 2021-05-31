package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXTRA_DISTANCE = 50;

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
        return DEFAULT_FARE + distanceTenToFifty(distance) + distanceMoreThanFifty(distance);
    }

    private int distanceTenToFifty(int distance) {
        distance = Math.min(distance, EXTRA_DISTANCE) - DEFAULT_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int distanceMoreThanFifty(int distance) {
        distance -= EXTRA_DISTANCE;
        if (distance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

}

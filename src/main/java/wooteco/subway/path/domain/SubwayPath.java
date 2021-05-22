package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int EXTRA_FARE_SECOND_BOUNDARY = 50;
    private static final int EXTRA_FARE_FIRST_BOUNDARY = 10;
    private static final int EXTRA_FARE_RATE_AFTER_SECOND_BOUNDARY = 8;
    private static final int EXTRA_FARE_RATE_AFTER_FIRST_BOUNDARY = 5;
    private static final int BASE_FARE = 1250;
    private static final int FARE_BETWEEN_10_TO_50 = 800;
    private static final int FARE_RATE = 100;
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
        return calculateFareByDistance() + calculateFareByLineExtraFare();
    }

    private int calculateFareByLineExtraFare() {
        int maxExtrafare = 0;
        for(Station station : stations){
            for(SectionEdge sectionEdge : sectionEdges){
                if(sectionEdge.getLine().getStations().contains(station)){
                    maxExtrafare = Math.max(sectionEdge.getLine().getExtraFare(), maxExtrafare);
                }
            }
        }
        return maxExtrafare;
    }

    public int calculateFareByDistance(){
        int totalDistance = calculateDistance();
        if (totalDistance > EXTRA_FARE_SECOND_BOUNDARY) {
            return BASE_FARE + FARE_BETWEEN_10_TO_50 + (int)
                    ((Math.ceil((totalDistance - EXTRA_FARE_SECOND_BOUNDARY - 1) /
                            EXTRA_FARE_RATE_AFTER_SECOND_BOUNDARY)) + 1) * FARE_RATE;
        }
        if (EXTRA_FARE_FIRST_BOUNDARY < totalDistance && totalDistance <= EXTRA_FARE_SECOND_BOUNDARY) {
            return BASE_FARE + (int)
                    ((Math.ceil((totalDistance - EXTRA_FARE_FIRST_BOUNDARY - 1) /
                            EXTRA_FARE_RATE_AFTER_FIRST_BOUNDARY)) + 1) * FARE_RATE;
        }
        return BASE_FARE;
    }
}

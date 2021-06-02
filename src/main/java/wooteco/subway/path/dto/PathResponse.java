package wooteco.subway.path.dto;

import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Map;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int defaultFare;
    private Map<String, Integer> fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int defaultFare, Map<String, Integer> fare) {
        this.stations = stations;
        this.distance = distance;
        this.defaultFare = defaultFare;
        this.fare = fare;
    }

    public static PathResponse of(List<StationResponse> stations, int distance, int defaultFare, FareResponse fare) {
        return new PathResponse(stations, distance, defaultFare, fare.getFare());
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getDefaultFare() {
        return defaultFare;
    }

    public Map<String, Integer> getFare() {
        return fare;
    }
}

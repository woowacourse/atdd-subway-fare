package wooteco.subway.path.dto;

import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int defaultFare;
    private FareResponse fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int defaultFare, FareResponse fare) {
        this.stations = stations;
        this.distance = distance;
        this.defaultFare = defaultFare;
        this.fare = fare;
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

    public FareResponse getFare() {
        return fare;
    }
}

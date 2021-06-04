package wooteco.subway.path.dto;

import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public final class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int extraFare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance) {
        this(stations, distance, 0);
    }

    public PathResponse(List<StationResponse> stationResponses, int distance, int extraFare) {
        this.stations = stationResponses;
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getExtraFare() {
        return extraFare;
    }
}

package wooteco.subway.path.dto;

import wooteco.subway.path.domain.fare.FareTable;
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

    public PathResponse(List<StationResponse> stations, int distance, FareTable fareTable) {
        this.stations = stations;
        this.distance = distance;
        this.defaultFare = fareTable.getDefaultFare();
        this.fare = fareTable.getFareTable();
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

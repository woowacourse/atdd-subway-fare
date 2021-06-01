package wooteco.subway.path.dto;

import wooteco.subway.path.domain.fare.FareTable;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int defaultFare;
    private FareResponse fareResponse;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, FareTable fareTable) {
        this.stations = stations;
        this.distance = distance;
        this.defaultFare = fareTable.getDefaultFare();
        this.fareResponse = FareResponse.of(fareTable);
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
}

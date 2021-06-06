package wooteco.subway.path.ui.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import wooteco.subway.station.ui.dto.StationResponse;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;
    private final int fare;

    @ConstructorProperties({"stations", "distance", "fare"})
    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}

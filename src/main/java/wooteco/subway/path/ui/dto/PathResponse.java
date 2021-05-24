package wooteco.subway.path.ui.dto;

import wooteco.subway.station.ui.dto.StationResponse;

import java.beans.ConstructorProperties;
import java.util.List;

public class PathResponse {

    private final List<StationResponse> stations;
    private final int distance;

    @ConstructorProperties({"stations", "distance"})
    public PathResponse(List<StationResponse> stations, int distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

}

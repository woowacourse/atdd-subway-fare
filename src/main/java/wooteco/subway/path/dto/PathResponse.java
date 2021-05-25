package wooteco.subway.path.dto;

import java.util.stream.Collectors;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public static PathResponse of(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = subwayPath.calculateFare();
        return new PathResponse(stationResponses, distance, fare);
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

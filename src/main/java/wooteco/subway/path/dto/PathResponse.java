package wooteco.subway.path.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

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

    public static PathResponse of(SubwayPath subwayPath, int defaultFare, Map<String, Integer> fare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        return new PathResponse(stationResponses, distance, defaultFare, fare);
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

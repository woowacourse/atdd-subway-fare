package wooteco.subway.path.dto;

import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private int defaultFare;
    private Map<String, Integer> fare;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance,
                        int defaultFare, Map<String, Integer> fare) {
        this.stations = stations;
        this.distance = distance;
        this.defaultFare = defaultFare;
        this.fare = fare;
    }

    public static PathResponse of(SubwayPath subwayPath, int distance,
                                  int defaultFare, Map<String, Integer> fare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(toList());

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

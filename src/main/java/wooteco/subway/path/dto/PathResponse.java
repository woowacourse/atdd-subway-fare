package wooteco.subway.path.dto;

import wooteco.subway.path.domain.Price;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponse {
    private List<StationResponse> stations;
    private int distance;
    private Price price;

    public PathResponse() {
    }

    public PathResponse(List<StationResponse> stations, int distance, Price price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price;
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price.getPrice();
    }
}

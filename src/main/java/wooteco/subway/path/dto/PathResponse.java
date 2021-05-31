package wooteco.subway.path.dto;

import java.util.List;
import wooteco.subway.path.domain.Price;
import wooteco.subway.station.dto.StationResponse;

public class PathResponse {

    private List<StationResponse> stations;
    private int distance;
    private int price;

    public PathResponse() {

    }

    public PathResponse(List<StationResponse> stations, int distance, Price price) {
        this.stations = stations;
        this.distance = distance;
        this.price = price.getValue();
    }

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }
}

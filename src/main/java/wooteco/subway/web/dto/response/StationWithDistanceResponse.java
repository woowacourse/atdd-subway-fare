package wooteco.subway.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.*;

public class StationWithDistanceResponse {

    private long id;
    private String name;
    @JsonInclude(Include.NON_DEFAULT)
    private int distance;

    public StationWithDistanceResponse() {
    }

    public StationWithDistanceResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationWithDistanceResponse(long id, String name, int distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }
}

package wooteco.subway.web.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class StationWithDistanceResponse {

    private Long id;
    private String name;
    @JsonInclude(Include.NON_DEFAULT)
    private int distance;

    public StationWithDistanceResponse() {
    }

    public StationWithDistanceResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StationWithDistanceResponse(Long id, String name, int distance) {
        this.id = id;
        this.name = name;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDistance() {
        return distance;
    }
}

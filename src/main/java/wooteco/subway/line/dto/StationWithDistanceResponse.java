package wooteco.subway.line.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class StationWithDistanceResponse {

    private long id;
    private String name;
    @JsonInclude(Include.NON_NULL)
    private Integer distance;

    public StationWithDistanceResponse() {
    }

    public StationWithDistanceResponse(long id, String name) {
        this(id, name, null);
    }

    public StationWithDistanceResponse(long id, String name, Integer distance) {
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

    public Integer getDistance() {
        return distance;
    }
}

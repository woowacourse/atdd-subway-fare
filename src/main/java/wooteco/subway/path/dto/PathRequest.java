package wooteco.subway.path.dto;

import javax.validation.constraints.NotNull;

public class PathRequest {
    @NotNull
    private final Long departure;
    @NotNull
    private final Long arrival;

    public PathRequest(long departure, long arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }

    public Long getDeparture() {
        return departure;
    }

    public Long getArrival() {
        return arrival;
    }
}

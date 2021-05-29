package wooteco.subway.line.dto;

import javax.validation.constraints.NotNull;

public class StationRemoveRequest {
    @NotNull
    private final Long stationId;

    public StationRemoveRequest(@NotNull Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}

package wooteco.subway.station.ui.dto;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.station.domain.Station;

public class StationResponse {

    private final Long id;
    private final String name;

    @ConstructorProperties({"id", "name"})
    public StationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }

    public static List<StationResponse> listOf(List<Station> stations) {
        return stations.stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

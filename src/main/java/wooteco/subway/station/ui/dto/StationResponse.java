package wooteco.subway.station.ui.dto;

import java.beans.ConstructorProperties;

import java.util.ArrayList;
import java.util.Collections;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

public class StationResponse {
    private final Long id;
    private final String name;
    private final List<LineResponse> lines;

    public static StationResponse of(Station station) {
        return new StationResponse(station.getId(), station.getName(), Collections.emptyList());
    }

    @ConstructorProperties({"id", "name", "lineResponses"})
    public StationResponse(Long id, String name, List<LineResponse> lines) {
        this.id = id;
        this.name = name;
        this.lines = new ArrayList<>(lines);
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

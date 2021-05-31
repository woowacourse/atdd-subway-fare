package wooteco.subway.station.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class StationResponse {
    private Long id;
    private String name;

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
}

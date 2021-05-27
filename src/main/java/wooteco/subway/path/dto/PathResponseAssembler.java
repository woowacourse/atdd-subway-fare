package wooteco.subway.path.dto;

import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {

    private PathResponseAssembler() {
    }

    public static PathResponse assemble(List<Station> stations, int distance, int fare) {
        List<StationResponse> stationResponses = stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        return new PathResponse(stationResponses, distance, fare);
    }
}

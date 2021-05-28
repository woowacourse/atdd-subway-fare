package wooteco.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

public class PathResponseAssembler {

    public static PathResponse assemble(SubwayPath subwayPath, int distance, Fare fare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
            .map(it -> StationResponse.of(it))
            .collect(Collectors.toList());

        return new PathResponse(stationResponses, distance, fare.value());
    }
}

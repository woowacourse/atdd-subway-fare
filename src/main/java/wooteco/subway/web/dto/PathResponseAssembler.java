package wooteco.subway.web.dto;

import wooteco.subway.domain.SubwayPath;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, int fare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        return new PathResponse(stationResponses, distance, fare);
    }
}

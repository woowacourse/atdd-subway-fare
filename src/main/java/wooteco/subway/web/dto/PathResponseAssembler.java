package wooteco.subway.web.dto;

import wooteco.subway.domain.SubwayPath;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.response.StationResponse;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        return new PathResponse(stationResponses, distance);
    }
}

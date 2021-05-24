package wooteco.subway.acceptance.path.dto;

import wooteco.subway.acceptance.path.domain.SubwayPath;
import wooteco.subway.acceptance.path.domain.SubwayPathFare;
import wooteco.subway.acceptance.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, SubwayPathFare subwayPathFare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, subwayPath.calculateDistance(), subwayPathFare.getFare());
    }
}

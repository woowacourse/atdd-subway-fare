package wooteco.subway.path.dto;

import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = StationResponse.listOf(subwayPath.stations());

        return new PathResponse(stationResponses, subwayPath.distance(), subwayPath.fare());
    }
}

package wooteco.subway.path.dto;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Integer fare, List<Line> persistLines) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(it -> StationResponse.of(it, persistLines))
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, subwayPath.calculateDistance(), fare);
    }
}

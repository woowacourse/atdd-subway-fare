package wooteco.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Integer age, List<Line> persistLines) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                                                           .map(it -> StationResponse.of(it, persistLines))
                                                           .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = subwayPath.calculateFare(distance, age);
        return new PathResponse(stationResponses, distance, fare);
    }
}

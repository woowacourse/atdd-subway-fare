package wooteco.subway.path.dto;

import wooteco.subway.line.domain.fare.AgeFarePolicy;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public final class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, AgeFarePolicy ageFarePolicy) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int extraFare = subwayPath.getExtraFare(ageFarePolicy);
        return new PathResponse(stationResponses, distance, extraFare);
    }
}

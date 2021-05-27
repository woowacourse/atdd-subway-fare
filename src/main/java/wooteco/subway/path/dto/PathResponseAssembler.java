package wooteco.subway.path.dto;

import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.ui.farepolicy.FarePolicy;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, FarePolicy farePolicy) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int extraFare = subwayPath.getExtraFare(farePolicy);
        return new PathResponse(stationResponses, distance, extraFare);
    }
}

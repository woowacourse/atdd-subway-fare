package wooteco.subway.path.dto;

import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.FareTable;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Fare fare, FareTable fareTable) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        return new PathResponse(stationResponses, distance, fare.money(), FareResponse.of(fareTable));
    }
}

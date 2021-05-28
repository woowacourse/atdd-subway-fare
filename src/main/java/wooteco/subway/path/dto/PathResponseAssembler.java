package wooteco.subway.path.dto;

import wooteco.subway.path.domain.FarePolicy;
import wooteco.subway.path.domain.FareTable;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(it -> StationResponse.of(it))
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        FarePolicy fare = FarePolicy.of(distance);
        FareTable fareTable = FareTable.of(fare);

        return new PathResponse(stationResponses, distance, fareTable);
    }
}

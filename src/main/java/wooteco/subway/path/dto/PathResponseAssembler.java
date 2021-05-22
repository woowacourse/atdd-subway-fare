package wooteco.subway.path.dto;

import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.SubwayPathFare;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int lineFare = subwayPath.getMaxLineFare();

        SubwayPathFare subwayPathFare = new SubwayPathFare(distance, lineFare);

        return new PathResponse(stationResponses, distance, subwayPathFare.getFare());
    }
}

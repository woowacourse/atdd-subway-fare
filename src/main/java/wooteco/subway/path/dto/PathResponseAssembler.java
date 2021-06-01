package wooteco.subway.path.dto;

import wooteco.subway.path.domain.SubwayFare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, SubwayFare subwayFare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = subwayFare.getFare();

        return new PathResponse(stationResponses, distance, fare);
    }
}

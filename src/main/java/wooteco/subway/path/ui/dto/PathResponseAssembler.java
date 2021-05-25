package wooteco.subway.path.ui.dto;

import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.ui.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {

    public static PathResponse assemble(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = subwayPath.fare().intValue();

        return new PathResponse(stationResponses, distance, fare);
    }

}

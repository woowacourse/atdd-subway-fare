package wooteco.subway.path.ui.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.ui.dto.StationResponse;

public class PathResponseAssembler {

    public static PathResponse assemble(SubwayPath subwayPath, int fare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        return new PathResponse(stationResponses, distance, fare);
    }

}

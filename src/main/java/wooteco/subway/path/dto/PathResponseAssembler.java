package wooteco.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.section.domain.Distance;
import wooteco.subway.station.domain.Stations;
import wooteco.subway.station.dto.StationResponse;

public class PathResponseAssembler {

    public static PathResponse assemble(SubwayPath subwayPath, Fare fare) {
        List<StationResponse> stationResponses = convertToResponses(subwayPath.getStations());
        Distance distance = subwayPath.calculateDistance();

        return new PathResponse(stationResponses, distance.getValue(), fare.getValue());
    }

    private static List<StationResponse> convertToResponses(Stations stations) {
        return stations.getStations()
            .stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());
    }
}

package wooteco.subway.path.dto;

import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {

    private PathResponseAssembler() {
    }

    public static PathResponse assemble(SubwayPath subwayPath, Fare fare) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int totalFare = fare.calculate(subwayPath.extraFare(), distance);

        return new PathResponse(stationResponses, distance, totalFare);
    }
}

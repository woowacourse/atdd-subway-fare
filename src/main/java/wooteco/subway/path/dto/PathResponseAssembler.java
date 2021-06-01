package wooteco.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.path.domain.Price;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

public class PathResponseAssembler {

    public static PathResponse assemble(SubwayPath subwayPath, Integer age) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        Price price = new Price(distance, subwayPath.findMaxExtraPrice(), age);

        return new PathResponse(stationResponses, distance, price);
    }
}

package wooteco.subway.path.dto;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.path.domain.Price;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
            .map(StationResponse::of)
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();

        Price price = new Price(0);
        price.calculatePrice(distance);
        price.addExtraPrice(subwayPath.findMaxExtraPrice());

        return new PathResponse(stationResponses, distance, price);
    }
}

package wooteco.subway.path.dto;

import wooteco.subway.member.domain.User;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.FareTable;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, User member) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int extraFare = subwayPath.calculateExtraFare();
        Fare fare = Fare.of(distance, extraFare);
        FareTable fareTable = FareTable.of(fare, member);

        return new PathResponse(stationResponses, distance, fareTable);
    }
}

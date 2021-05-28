package wooteco.subway.path.dto;

import wooteco.subway.member.domain.Member;
import wooteco.subway.path.domain.FarePolicy;
import wooteco.subway.path.domain.FareTable;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, Member member) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        FarePolicy fare = FarePolicy.of(distance);
        FareTable fareTable = FareTable.of(fare);
        int defaultFare = fareTable.findByAge(fare, member.getAge());

        return new PathResponse(stationResponses, distance, defaultFare, fareTable);
    }
}

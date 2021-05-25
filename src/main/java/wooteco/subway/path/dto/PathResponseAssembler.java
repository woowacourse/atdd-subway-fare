package wooteco.subway.path.dto;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(LoginMember member, SubwayPath subwayPath) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = subwayPath.calculateFare(member, distance);
        return new PathResponse(stationResponses, distance, fare);
    }
}

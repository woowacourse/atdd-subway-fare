package wooteco.subway.path.dto;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, LoginMember loginMember) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.distance();
        int fare = subwayPath.subwayFare(loginMember.getAge());
        return new PathResponse(stationResponses, distance, fare);
    }

}

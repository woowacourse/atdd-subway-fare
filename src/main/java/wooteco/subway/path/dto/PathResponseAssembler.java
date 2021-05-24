package wooteco.subway.path.dto;

import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

public class PathResponseAssembler {
    public static PathResponse assemble(SubwayPath subwayPath, AuthMember authMember) {
        List<StationResponse> stationResponses = subwayPath.getStations().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = authMember.getDiscountedFare(subwayPath.calculateFare());

        return new PathResponse(stationResponses, distance, fare);
    }
}

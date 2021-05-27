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

        int distance = subwayPath.calculateDistance();
        int fare = subwayPath.calculateFare();
        return new PathResponse(stationResponses, distance, calculateDiscountFare(fare, loginMember.getAge()));
    }

    private static int calculateDiscountFare(int fare, int age) {
        if (age >= 6 && age < 13) {
            return (int) ((fare - 350) * 0.5);
        }

        if (age >= 13 && age < 19) {
            return (int) ((fare - 350) * 0.8);
        }
        return fare;
    }

}

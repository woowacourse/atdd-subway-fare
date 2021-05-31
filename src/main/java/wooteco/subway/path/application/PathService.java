package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.member.domain.MemberType;
import wooteco.subway.path.domain.FarePolicy;
import wooteco.subway.path.domain.SubwayRoute;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;

@Service
@Transactional
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private RouteFinder routeFinder;

    public PathService(LineService lineService, StationService stationService, RouteFinder routeFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.routeFinder = routeFinder;
    }

    public PathResponse findPath(Long sourceId, Long targetId, MemberType memberType) {
        SubwayRoute route = findRoute(sourceId, targetId);

        return getPathResponse(route, memberType);
    }

    private SubwayRoute findRoute(Long sourceId, Long targetId) {
        try {
            Station source = stationService.findStationById(sourceId);
            Station target = stationService.findStationById(targetId);
            return routeFinder.find(lineService.findLines(), source, target);
        } catch (Exception e) {
            throw new PathException("적절하지 않은 구간의 경로 탐색입니다.");
        }
    }

    private PathResponse getPathResponse(SubwayRoute route, MemberType memberType) {
        int fare = FarePolicy.calculate(route.distance(), route.extraFare(), memberType);
        List<StationResponse> stationResponses = stationService.stationResponses(route.stations());

        return new PathResponse(stationResponses, route.distance(), fare);
    }
}

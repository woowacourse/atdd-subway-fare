package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.MemberType;
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
    private FareService fareService;

    public PathService(LineService lineService, StationService stationService, RouteFinder routeFinder, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.routeFinder = routeFinder;
        this.fareService = fareService;
    }

    public PathResponse findPath(Long source, Long target, MemberType memberType) {
        SubwayRoute route = findRoute(source, target);

        List<StationResponse> stationResponses = stationService.toStationResponses(route.stations());
        int fare = fareService.calculate(route.distance(), route.extraFare(), memberType);

        return new PathResponse(stationResponses, route.distance(), fare);
    }

    private SubwayRoute findRoute(Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            return routeFinder.find(lines, sourceStation, targetStation);
        } catch (Exception e) {
            throw new LineException("검색된 경로가 없습니다.");
        }
    }
}

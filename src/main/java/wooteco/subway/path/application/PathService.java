package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

@Service
@Transactional
public class PathService {

    private LineService lineService;
    private StationService stationService;
    private PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService,
        PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            List<StationResponse> stationResponses = StationResponse
                .listOf(subwayPath.getStations());
            int distance = subwayPath.calculateDistance();
            Fare maxExtraFare = subwayPath.calculateMaxExtraFare();
            Fare totalFare = Fare.ofSubwayFare(distance, maxExtraFare, loginMember.getAge());
//            Fare totalFare = subwayPath.fareOf(loginMember);

            return new PathResponse(stationResponses, distance, totalFare);
        } catch (Exception e) {
            throw new InvalidPathException("출발지와 도착지는 같을 수 없습니다.");
        }
    }
}

package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
@Transactional
public class PathService {
    private static final int DEFAULT_FARE = 1250;
    private LineService lineService;
    private StationService stationService;
    private PathFinder pathFinder;
    private FareCalculator fareCalculator;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.fareCalculator = new FareCalculator();
    }

    public PathResponse findPath(Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            Fare fareByDistance = fareCalculator.getExtraFareByDistance(new Fare(DEFAULT_FARE), subwayPath.calculateDistance());
            Fare fareWithLineExtraFare = fareCalculator.getFareWithLineExtraFare(fareByDistance, subwayPath.getLines());
            // TODO: 로그인 되어있을 경우, 사용자 나이로 할인 적용
            return PathResponseAssembler.assemble(subwayPath, fareWithLineExtraFare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

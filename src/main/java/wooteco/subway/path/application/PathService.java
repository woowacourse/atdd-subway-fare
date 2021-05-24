package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.FareCalculator;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
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

            int totalFare = calculateTotalFare(loginMember, subwayPath);
            return PathResponseAssembler.assemble(subwayPath, totalFare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private int calculateTotalFare(LoginMember loginMember, SubwayPath subwayPath) {
        final int basicFare = FareCalculator.calculateFare(subwayPath.calculateDistance());
        final int extraFareWithLine = FareCalculator.calculateFareWithLine(basicFare, subwayPath.getLines());
        if (loginMember.isLogin()) {
            return FareCalculator.discountFareByAge(extraFareWithLine, loginMember.getAge());
        }
        return extraFareWithLine;
    }
}

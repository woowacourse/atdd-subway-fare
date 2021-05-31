package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.InvalidInputException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.User;
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

    public PathResponse findPath(User user, Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            int totalFare = calculateTotalFare(user, subwayPath);
            return PathResponseAssembler.assemble(subwayPath, totalFare);
        } catch (Exception e) {
            throw new InvalidInputException("경로를 찾을 수 없습니다.");
        }
    }

    private int calculateTotalFare(User user, SubwayPath subwayPath) {
        final int basicFare = FareCalculator.calculateFare(subwayPath.calculateDistance());
        final int extraFareWithLine = FareCalculator.calculateFareWithLine(basicFare, subwayPath.getLines());
        if (user.isLogin()) {
            return FareCalculator.discountFareByAge(extraFareWithLine, user.getAge());
        }
        return extraFareWithLine;
    }
}

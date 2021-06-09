package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.creationstrategy.DistanceBasedCreationStrategy;
import wooteco.subway.path.domain.fare.discountrule.AgeBasedDiscountRule;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Transactional
@Service
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target, LoginMember member) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findExistentStationById(source);
            Station targetStation = stationService.findExistentStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            Fare fare = calculateFare(subwayPath, member.getAge());

            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException(e.getMessage());
        }
    }

    private Fare calculateFare(SubwayPath subwayPath, int age) {
        return subwayPath.calculateFare(
            new DistanceBasedCreationStrategy()
        ).applyDiscountRule(
            AgeBasedDiscountRule.from(age)
        );
    }
}

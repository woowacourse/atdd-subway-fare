package wooteco.subway.path.application;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.InvalidPathException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.policy.FarePolicyFactory;
import wooteco.subway.path.ui.dto.PathResponse;
import wooteco.subway.path.ui.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService,
        PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            Fare fare = new Fare(FarePolicyFactory.createExtraFarePolicy(),
                FarePolicyFactory.createDiscountPolicy());
            BigDecimal calculatedFare = fare.calculate(subwayPath, loginMember);

            return PathResponseAssembler.assemble(subwayPath, calculatedFare.intValue());
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

}

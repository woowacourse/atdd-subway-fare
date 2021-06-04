package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.InvalidPathException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.age.AgeStrategy;
import wooteco.subway.path.domain.fare.age.AgeType;
import wooteco.subway.path.domain.fare.distance.DistanceChain;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
@Transactional
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final DistanceChain defaultChain;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder, DistanceChain defaultChain) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.defaultChain = defaultChain;
    }

    @Transactional(readOnly = true)
    public PathResponse findPath(Long departure, Long arrival, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station departureStation = stationService.findStationById(departure);
            Station arrivalStation = stationService.findStationById(arrival);
            SubwayPath subwayPath = pathFinder.findPath(lines, departureStation, arrivalStation);

            Fare fare = generateFare(loginMember);

            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException(e.getMessage());
        }
    }

    private Fare generateFare(LoginMember loginMember) {
        AgeStrategy ageStrategy = AgeType.ageStrategy(loginMember.getAge());
        return new Fare(defaultChain, ageStrategy);
    }
}

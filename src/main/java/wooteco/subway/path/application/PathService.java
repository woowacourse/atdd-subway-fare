package wooteco.subway.path.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.LineExtraFareCalculator;
import wooteco.subway.path.domain.fare.age.AgeFare;
import wooteco.subway.path.domain.fare.distance.DistanceFare;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    private static final int DEFAULT_FARE = 1250;

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final LineExtraFareCalculator lineExtraFareCalculator;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.lineExtraFareCalculator = new LineExtraFareCalculator();
    }

    public PathResponse findPath(Long source, Long target, Optional<LoginMember> loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            Fare totalFare = getTotalFare(loginMember, subwayPath);
            return PathResponseAssembler.assemble(subwayPath, totalFare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private Fare getTotalFare(Optional<LoginMember> loginMemberOptional, SubwayPath subwayPath) {
        Fare fareByDistance = getFareByDistance(subwayPath);
        Fare fareWithLineExtraFare = lineExtraFareCalculator.getFare(fareByDistance, subwayPath.getLines());
        if (!loginMemberOptional.isPresent()) {
            return fareWithLineExtraFare;
        }
        LoginMember loginMember = loginMemberOptional.get();
        return getFareByAge(fareWithLineExtraFare, loginMember);
    }

    private Fare getFareByAge(Fare fareWithLineExtraFare, LoginMember loginMember) {
        AgeFare ageFare = AgeFare.of(loginMember.getAge());
        return ageFare.getFare(fareWithLineExtraFare);
    }

    private Fare getFareByDistance(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();
        DistanceFare distanceFare = DistanceFare.of(distance);
        return distanceFare.getFare(new Fare(DEFAULT_FARE));
    }
}

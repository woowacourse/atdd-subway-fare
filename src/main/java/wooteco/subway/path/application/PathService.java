package wooteco.subway.path.application;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.domain.fare.FareCalculator;
import wooteco.subway.path.domain.fare.distance.DistanceFare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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

//    private Fare getTotalFareNew(Optional<LoginMember> loginMemberOptional, SubwayPath subwayPath) {
//        int distance = subwayPath.calculateDistance();
//        FareDistance fareDistance = FareDistance.of(distance);
//        Fare fareByDistance = fareDistance.getFare(new Fare(DEFAULT_FARE));
//        Fare fareWithLineExtraFare = fareCalculator.getFareWithLineExtraFare(fareByDistance, subwayPath.getLines());
//        if (!loginMemberOptional.isPresent()) {
//            return fareWithLineExtraFare;
//        }
//        LoginMember loginMember = loginMemberOptional.get();
//        FareAge fareAge = FareAge.of(loginMember.getAge());
//        return fareCalculator.getFareByAge(fareWithLineExtraFare, fareAge);
//    }

    private Fare getTotalFare(Optional<LoginMember> loginMemberOptional, SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();
        DistanceFare distanceFare = DistanceFare.of(distance);
        Fare fareByDistance = distanceFare.getFare(new Fare(DEFAULT_FARE));
        Fare fareWithLineExtraFare = fareCalculator.getFareWithLineExtraFare(fareByDistance, subwayPath.getLines());
        if (!loginMemberOptional.isPresent()) {
            return fareWithLineExtraFare;
        }
        LoginMember loginMember = loginMemberOptional.get();
        return fareCalculator.getFareByAge(loginMember.getAge(), fareWithLineExtraFare);
    }
}

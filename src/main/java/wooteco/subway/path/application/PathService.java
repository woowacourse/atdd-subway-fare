package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.fare.domain.Fare;
import wooteco.subway.fare.domain.Money;
import wooteco.subway.fare.domain.farestrategy.AgeFare;
import wooteco.subway.fare.domain.farestrategy.DistanceFare;
import wooteco.subway.fare.domain.farestrategy.FarePolicy;
import wooteco.subway.fare.domain.farestrategy.LineFare;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

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

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
        return getPathResponse(loginMember, subwayPath);
    }

    private PathResponse getPathResponse(LoginMember loginMember, SubwayPath subwayPath) {
        List<Station> stations = subwayPath.getStations();
        int distance = subwayPath.calculateDistance();
        int lineExtraFare = subwayPath.calculateLineFare();
        if (Objects.isNull(loginMember)) {
            FarePolicy farePolicy = new FarePolicy.Builder()
                    .linePolicy(new LineFare(lineExtraFare))
                    .distancePolicy(new DistanceFare(distance))
                    .build();
            Fare fare = new Fare(Money.DEFAULT_MONEY, farePolicy);
            return PathResponseAssembler.assemble(stations, distance, fare.calculate());
        }

        FarePolicy farePolicy = new FarePolicy.Builder()
                .linePolicy(new LineFare(lineExtraFare))
                .distancePolicy(new DistanceFare(distance))
                .agePolicy(new AgeFare(loginMember.getAge()))
                .build();
        Fare fare = new Fare(Money.DEFAULT_MONEY, farePolicy);
        return PathResponseAssembler.assemble(stations, distance, fare.calculate());
    }
}

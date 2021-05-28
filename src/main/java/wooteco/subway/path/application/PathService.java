package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.fare.domain.*;
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
        return writePathResponse(loginMember, subwayPath);
    }

    private PathResponse writePathResponse(LoginMember loginMember, SubwayPath subwayPath) {
        List<Station> stations = subwayPath.getStations();
        int distance = subwayPath.calculateDistance();
        Fare fare = generateFare(loginMember, subwayPath);
        return PathResponseAssembler.assemble(stations, distance, fare.calculateFare());
    }

    private Fare generateFare(LoginMember loginMember, SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();
        int lineExtraFare = subwayPath.calculateLineFare();
        FareStrategy fareStrategy = DistanceFareStrategy.find(distance);
        if (Objects.isNull(loginMember)) {
            return new Fare(distance, lineExtraFare, fareStrategy);
        }
        DiscountStrategy discountStrategy = AgeDiscountStrategy.find(loginMember.getAge());
        return new Fare(distance, lineExtraFare, fareStrategy, discountStrategy);
    }
}

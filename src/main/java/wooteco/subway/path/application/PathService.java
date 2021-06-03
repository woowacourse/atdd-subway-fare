package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.fare.domain.*;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.domain.SubwayPath;
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

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target, User user) {
        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
        return writePathResponse(user, subwayPath);
    }

    private PathResponse writePathResponse(User user, SubwayPath subwayPath) {
        List<Station> stations = subwayPath.getStations();
        int distance = subwayPath.calculateDistance();
        int age = user.getAge();
        FareStrategy fareStrategy = DistanceFareStrategy.find(distance);
        DiscountStrategy discountStrategy = AgeDiscountStrategy.find(age);
        Fare fare = new Fare(subwayPath, fareStrategy, discountStrategy);
        return PathResponseAssembler.assemble(stations, distance, fare.calculateFare());
    }
}

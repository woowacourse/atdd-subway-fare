package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final FareService fareService;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder, FareService fareService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.fareService = fareService;
    }

    public PathResponse findPath(User user, Long source, Long target) {
        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);
        SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

        int totalFare = calculateTotalFare(user, subwayPath);
        return PathResponseAssembler.assemble(subwayPath, totalFare);
    }

    private int calculateTotalFare(User user, SubwayPath subwayPath) {
        final int fare = fareService.calculateFare(subwayPath.calculateDistance());
        final int addedFare = fareService.calculateExtraFare(fare, subwayPath.getLines());
        if (!user.isGuest()) {
            return fareService.discountFareByAge(addedFare, user.getAge());
        }
        return addedFare;
    }
}

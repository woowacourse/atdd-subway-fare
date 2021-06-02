package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.RequestUser;
import wooteco.subway.path.domain.DiscountPolicy;
import wooteco.subway.path.domain.FareTable;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(RequestUser loginMember,Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            FareTable fareTable = new FareTable();
            fareTable.calculateFare(subwayPath.getSectionEdges(), subwayPath.calculateDistance());
            int defaultFare = fareTable.findByAge(DiscountPolicy.findAge(loginMember.getAge()).getKorean());

            return PathResponse.of(subwayPath, defaultFare, fareTable.getFareTable());
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.domain.DiscountPolicy;
import wooteco.subway.path.domain.FareTable;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Map;

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

    public PathResponse findPath(User member, Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            FareTable fareTable = getFareTable(subwayPath);

            int distance = subwayPath.calculateDistance();
            int defaultFare = fareTable.calculateByAgeType(DiscountPolicy.findAge(member.getAge()).getKorean());
            Map<String, Integer> fare = fareTable.getFareTable();

            return PathResponse.of(subwayPath, distance, defaultFare, fare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private FareTable getFareTable(SubwayPath subwayPath) {
        FareTable fareTable = new FareTable();
        fareTable.make(subwayPath.calculateDistance(), subwayPath.getSectionEdges());
        return fareTable;
    }
}

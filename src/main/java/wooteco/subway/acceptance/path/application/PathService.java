package wooteco.subway.acceptance.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.acceptance.line.application.LineService;
import wooteco.subway.acceptance.line.domain.Line;
import wooteco.subway.acceptance.member.domain.LoginMember;
import wooteco.subway.acceptance.path.domain.SubwayPath;
import wooteco.subway.acceptance.path.domain.SubwayPathFare;
import wooteco.subway.acceptance.path.dto.PathResponse;
import wooteco.subway.acceptance.path.dto.PathResponseAssembler;
import wooteco.subway.acceptance.station.application.StationService;
import wooteco.subway.acceptance.station.domain.Station;

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

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            int distance = subwayPath.calculateDistance();
            int lineFare = subwayPath.getMaxLineFare();
            SubwayPathFare subwayPathFare = new SubwayPathFare(loginMember.getAge(), distance, lineFare);
            return PathResponseAssembler.assemble(subwayPath, subwayPathFare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayFare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);

            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            SubwayFare subwayFare = new SubwayFare(
                    existLines(subwayPath), subwayPath.calculateDistance(), loginMember
            );

            return PathResponseAssembler.assemble(subwayPath, subwayFare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private Set<Line> existLines(SubwayPath subwayPath) {
        return subwayPath.getSectionEdges().stream()
                .map(SectionEdge::getLine)
                .collect(Collectors.toSet());
    }
}

package wooteco.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.InvalidPathException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.section.application.SectionService;
import wooteco.subway.section.domain.Sections;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional
public class PathService {

    private final SectionService sectionService;
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;

    public PathService(SectionService sectionService, StationService stationService, LineService lineService, PathFinder pathFinder) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target) {
        try {
            Station sourceStation = stationService.findById(source);
            Station targetStation = stationService.findById(target);
            List<Sections> sectionsList = lineService.findAll()
                .stream()
                .map(sectionService::findSectionsByLine)
                .collect(Collectors.toList());

            SubwayPath subwayPath = pathFinder.findPath(sectionsList, sourceStation, targetStation);
            return PathResponseAssembler.assemble(subwayPath);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.common.exception.badrequest.InvalidPathException;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.SubwayPath;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.PathResponseAssembler;
import wooteco.subway.domain.Station;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public PathService(LineService lineService, StationService stationService,
        PathFinder pathFinder, FareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            final int fare = fareCalculator.calculateFare(subwayPath.calculateDistance());
            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

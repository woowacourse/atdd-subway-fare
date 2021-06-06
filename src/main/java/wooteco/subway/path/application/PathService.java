package wooteco.subway.path.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;

    @Transactional
    public PathResponse findPath(Long source, Long target, int age) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            Fare fare = new Fare(subwayPath.calculateDistance(), subwayPath.calculateExtraLineFare(), age);
            return PathResponseAssembler.assemble(subwayPath, fare.getFare());
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

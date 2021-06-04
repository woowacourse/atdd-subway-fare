package wooteco.subway.path.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.InvalidPathException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.section.application.SectionService;
import wooteco.subway.section.domain.Sections;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionService sectionService;
    private final StationService stationService;
    private final LineService lineService;
    private final PathFinder pathFinder;
    private final FareService fareService;

    public PathService(SectionService sectionService, StationService stationService, LineService lineService, PathFinder pathFinder,
        FareService fareService) {
        this.sectionService = sectionService;
        this.stationService = stationService;
        this.lineService = lineService;
        this.pathFinder = pathFinder;
        this.fareService = fareService;
    }

    public PathResponse findPath(User user, Long source, Long target) {
        try {
            Station sourceStation = stationService.findById(source);
            Station targetStation = stationService.findById(target);
            List<Sections> sectionsList = lineService.findAll()
                .stream()
                .map(sectionService::findSectionsByLine)
                .collect(Collectors.toList());

            SubwayPath subwayPath = pathFinder.findPath(sectionsList, sourceStation, targetStation);
            Fare fare = calculateTotalFare(user, subwayPath);
            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private Fare calculateTotalFare(User user, SubwayPath subwayPath) {
        Fare fare = fareService.calculateFare(subwayPath.calculateDistance());
        Fare extraFare = fareService.calculateExtraFare(fare, subwayPath.getLines());

        try {
            return fareService.discountFareByAge(user.getAge(), extraFare.getValue());
        } catch (AuthorizationException e) {
            return extraFare;
        }
    }

}

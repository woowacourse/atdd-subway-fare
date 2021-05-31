package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.domain.fare.FarePolicyFinder;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;

@Service
@Transactional
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private PathFinder pathFinder;
    private FarePolicyFinder farePrincipalFinder;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder,
                       FarePolicyFinder farePrincipalFinder) {

        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.farePrincipalFinder = farePrincipalFinder;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            return PathResponseAssembler.assemble(subwayPath,
                    farePrincipalFinder.findFarePrincipal(loginMember.getAge()));
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

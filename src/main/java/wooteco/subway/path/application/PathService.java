package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.infrastructure.JwtTokenProvider;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.path.domain.SubwayPath;
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
    private AuthService authService;
    private PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService,
        AuthService authService, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.authService = authService;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(Long source, Long target, String accessToken) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);

            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            LoginMember loginMember = authService.findMemberByToken(accessToken);
            return PathResponseAssembler.assemble(subwayPath, loginMember);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

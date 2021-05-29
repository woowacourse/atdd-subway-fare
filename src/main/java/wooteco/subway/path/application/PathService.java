package wooteco.subway.path.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.auth.application.AuthService;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Price;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

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

            int distance = subwayPath.calculateDistance();
            LoginMember loginMember = authService.findMemberByToken(accessToken);
            // Price price = new Price(subwayPath.findMaxExtraPrice(), loginMember);

            Price price = new Price(0);
            price.calculatePrice(distance);
            price.addExtraPrice(subwayPath.findMaxExtraPrice());
            price.calculateDiscountRateFromAge(loginMember);    //ok

            return PathResponseAssembler.assemble(subwayPath, distance, price);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }
}

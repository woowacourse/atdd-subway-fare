package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.fare.domain.FareByAge;
import wooteco.subway.fare.domain.FareByDistance;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.domain.Member;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private MemberDao memberDao;
    private PathFinder pathFinder;

    public PathService(LineService lineService, StationService stationService, MemberDao memberDao, PathFinder pathFinder) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberDao = memberDao;
        this.pathFinder = pathFinder;
    }

    public PathResponse findPath(LoginMember loginMember, Long source, Long target) {
        List<Line> lines = lineService.findLines();
        Station sourceStation = stationService.findStationById(source);
        Station targetStation = stationService.findStationById(target);

        SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
        int fare = calculateFare(loginMember, subwayPath);
        return PathResponseAssembler.assemble(subwayPath, fare);
    }

    private int calculateFare(LoginMember loginMember, SubwayPath subwayPath) {
        int lineExtraFare = subwayPath.getSectionEdges().stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElseThrow(IllegalStateException::new);

        final int fareWithoutDiscount = FareByDistance.calculate(subwayPath.calculateDistance()) + lineExtraFare;

        if (isGuest(loginMember)) {
            return fareWithoutDiscount;
        }
        Member member = memberDao.findByEmail(loginMember.getEmail());
        return FareByAge.calculate(member.getAge(), fareWithoutDiscount);
    }

    private boolean isGuest(LoginMember loginMember) {
        return Objects.isNull(loginMember.getEmail());
    }
}

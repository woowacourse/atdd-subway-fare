package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Member;
import wooteco.common.exception.badrequest.InvalidPathException;
import wooteco.common.exception.notfound.MemberNotFoundException;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayPath;
import wooteco.subway.web.dto.PathResponseAssembler;
import wooteco.subway.web.dto.response.PathResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final LineService lineService;
    private final StationService stationService;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;
    private final MemberDao memberDao;

    public PathService(LineService lineService, StationService stationService,
        PathFinder pathFinder, FareCalculator fareCalculator, MemberDao memberDao) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
        this.memberDao = memberDao;
    }

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);
            FareCalculator fareCalculator = checkMemberAge(loginMember);

            final int fare = fareCalculator.calculateFare(
                subwayPath.calculateDistance(),
                subwayPath.calculateMaxExtraFare()
            );
            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private FareCalculator checkMemberAge(LoginMember loginMember) {
        if (loginMember.isAnonymous()) {
            return fareCalculator;
        }
        final Member member = memberDao.findById(loginMember.getId())
            .orElseThrow(MemberNotFoundException::new);
        return new AgeDiscountFareCalculator(fareCalculator, member.getAge());
    }
}

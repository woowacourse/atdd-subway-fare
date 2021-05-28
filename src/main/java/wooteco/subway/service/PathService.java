package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Member;
import wooteco.common.exception.badrequest.InvalidPathException;
import wooteco.common.exception.badrequest.MemberNotFoundException;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayPath;
import wooteco.subway.web.dto.PathResponseAssembler;
import wooteco.subway.web.dto.response.PathResponse;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {
    private final LineService lineService;
    private final StationService stationService;
    private final MemberDao memberDao;
    private final PathFinder pathFinder;
    private final FareCalculator fareCalculator;

    public PathService(LineService lineService, StationService stationService, MemberDao memberDao, PathFinder pathFinder, DefaultFareCalculator fareCalculator) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberDao = memberDao;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            FareCalculator fareCalculator = checkMemberAge(loginMember);

            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

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

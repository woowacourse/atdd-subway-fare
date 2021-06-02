package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.auth.dao.MemberDao;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.Member;
import wooteco.common.exception.badrequest.InvalidPathException;
import wooteco.common.exception.notfound.MemberNotFoundException;
import wooteco.subway.domain.*;
import wooteco.subway.web.dto.response.PathResponse;
import wooteco.subway.web.dto.PathResponseAssembler;

import java.util.List;

@Service
@Transactional
public class PathService {
    private LineService lineService;
    private StationService stationService;
    private PathFinder pathFinder;
    private MemberDao memberDao;

    public PathService(LineService lineService, StationService stationService, PathFinder pathFinder, MemberDao memberDao) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.memberDao = memberDao;
    }

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            FareCalculator fareCalculator = decideFareCalculator(loginMember);
            int fare = fareCalculator.calculateFare(
                    subwayPath.calculateDistance(),
                    subwayPath.calculateMaxExtraFare());
            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private FareCalculator decideFareCalculator(LoginMember loginMember) {
        if (loginMember.isAnonymous()) {
            return new DefaultFareCalculator();
        }
        Member member = memberDao.findById(loginMember.getId())
                .orElseThrow(MemberNotFoundException::new);
        return new AgeDiscountFareCalculator(new DefaultFareCalculator(), member.getAge());
    }
}

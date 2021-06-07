package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.auth.domain.LoginMember;
import wooteco.auth.domain.User;
import wooteco.auth.service.MemberService;
import wooteco.common.exception.badrequest.BadRequestException;
import wooteco.common.exception.badrequest.BadRequestException.BadRequestMessage;
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
    private final MemberService memberService;

    public PathService(LineService lineService, StationService stationService,
        PathFinder pathFinder,
        MemberService memberService) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.pathFinder = pathFinder;
        this.memberService = memberService;
    }

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            final User user = memberService.selectMemberOrAnonymous(loginMember);
            final int fare = user.calculateFee(
                subwayPath.calculateDistance(),
                subwayPath.calculateMaxExtraFare()
            );

            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new BadRequestException(BadRequestMessage.INVALID_PATH);
        }
    }
}

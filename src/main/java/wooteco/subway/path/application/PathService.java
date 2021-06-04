package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.exception.badrequest.InvalidPathException;
import wooteco.subway.line.application.LineService;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.application.MemberService;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.member.dto.MemberResponse;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.path.dto.PathResponseAssembler;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private LineService lineService;
    private StationService stationService;
    private MemberService memberService;
    private PathFinder pathFinder;
    private FareCalculator fareCalculator;

    public PathService(
        LineService lineService,
        StationService stationService,
        MemberService memberService,
        PathFinder pathFinder,
        FareCalculator fareCalculator
    ) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.memberService = memberService;
        this.pathFinder = pathFinder;
        this.fareCalculator = fareCalculator;
    }

    public PathResponse findPath(Long source, Long target, LoginMember loginMember) {
        try {
            List<Line> lines = lineService.findLines();
            Station sourceStation = stationService.findStationById(source);
            Station targetStation = stationService.findStationById(target);
            SubwayPath subwayPath = pathFinder.findPath(lines, sourceStation, targetStation);

            int fare = calculateFare(loginMember, subwayPath);
            return PathResponseAssembler.assemble(subwayPath, fare);
        } catch (Exception e) {
            throw new InvalidPathException();
        }
    }

    private int calculateFare(LoginMember loginMember, SubwayPath subwayPath) {
        FareCalculator fareCalculator = this.fareCalculator;

        if (loginMember.isMember()) {
            MemberResponse member = memberService.findMember(loginMember);
            fareCalculator = new AgeFareCalculator(fareCalculator, member.getAge());
        }

        return fareCalculator.calculateFare(
            subwayPath.calculateDistance(),
            subwayPath.calculateMaxExtraFare()
        );
    }
}

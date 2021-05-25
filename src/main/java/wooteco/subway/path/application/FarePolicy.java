package wooteco.subway.path.application;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;

import java.util.Optional;

public interface FarePolicy {
    Fare getTotalFare(SubwayPath subwayPath, Optional<LoginMember> loginMember);
}

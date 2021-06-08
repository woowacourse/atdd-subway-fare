package wooteco.subway.path.application;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.SubwayPath;

public interface FarePolicy {
    Fare BASIC_FARE = new Fare(1250);

    Fare calculateTotalFare(SubwayPath subwayPath, LoginMember loginMember);
}

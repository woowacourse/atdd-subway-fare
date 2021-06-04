package wooteco.subway.line.domain.fare;

import wooteco.subway.line.domain.fare.policy.*;
import wooteco.subway.member.domain.LoginMember;

public class FarePolicyFactory {

    public static FarePolicy findPolicy(LoginMember member) {
        if (member.isGuest() || member.isAdult()) {
            return new AdultFarePolicy();
        }

        if (member.isTeenager()) {
            return new TeenagerFarePolicy();
        }

        if (member.isChild()) {
            return new ChildFarePolicy();
        }

        return new InfantFarePolicy();
    }
}

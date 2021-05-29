package wooteco.subway.member.domain;

import wooteco.subway.path.domain.Fare;

public interface Member {
    Long getId();

    String getEmail();

    Integer getAge();

    Fare calculateFare(Fare fare);
}

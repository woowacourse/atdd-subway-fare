package wooteco.subway.member.domain;

import wooteco.subway.fare.domain.AgeFarePolicy;

public interface User {
    Long getId();

    String getEmail();

    Integer getAge();

    AgeFarePolicy farePolicy();

    boolean isOverThan(int age);
}

package wooteco.subway.member.domain;

import wooteco.subway.line.domain.fare.AgeFarePolicy;

public interface User {
    Long getId();
    String getEmail();
    Integer getAge();
    AgeFarePolicy farePolicy();
    boolean isOverThan(int age);
}

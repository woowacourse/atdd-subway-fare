package wooteco.subway.member.domain;

public interface Member {
    Long getId();

    String getEmail();

    Integer getAge();

    int calculateFare(int fare);
}

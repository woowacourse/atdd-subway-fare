package wooteco.subway.member.domain;

public interface User {

    boolean isGuest();

    long getId();

    String getEmail();

    int getAge();
}

package wooteco.subway.member.domain;

public interface User {
    Long getId();

    String getEmail();

    String getPassword();

    Integer getAge();

    boolean isGuest();
}

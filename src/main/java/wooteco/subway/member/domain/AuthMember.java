package wooteco.subway.member.domain;

public interface AuthMember {

    boolean isLoggedIn();

    Long getId();

    String getEmail();

    Integer getAge();

    int getDiscountedFare(int baseFare);
}

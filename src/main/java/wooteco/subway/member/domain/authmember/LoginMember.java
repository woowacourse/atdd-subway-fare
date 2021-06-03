package wooteco.subway.member.domain.authmember;

import wooteco.subway.fare.domain.FareStrategy;

public class LoginMember implements AuthMember {

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public int discountFareByAge(int fare) {
        return FareStrategy.distinguishAgeMember(age).discountFareByAge(fare);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}

package wooteco.subway.member.domain;

import wooteco.subway.path.domain.Fare;

import java.util.Objects;

public class LoginMember {
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

    public Fare calculateFare(Fare fare) {
        if (Objects.isNull(age)){
            return AgeFarePolicy.DEFAULT.calculateFare(fare);
        }
        AgeFarePolicy myAgePolicy = AgeFarePolicy.of(this.age);
        return myAgePolicy.calculateFare(fare);
    }
}

package wooteco.subway.member.domain;

import wooteco.subway.path.domain.Fare;

import java.util.Objects;

import static wooteco.subway.member.domain.Role.GUEST;
import static wooteco.subway.member.domain.Role.LOGIN;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private Role role;

    public LoginMember(Long id, String email, Integer age){
        this(id, email, age, LOGIN);
    }

    public LoginMember(Long id, String email, Integer age, Role role) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.role = role;
    }

    public static LoginMember createGuestMember(){
        return new LoginMember(null, null, null, GUEST);
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
        if (role == GUEST){
            return AgeFarePolicy.DEFAULT.calculateFare(fare);
        }
        AgeFarePolicy myAgePolicy = AgeFarePolicy.of(this.age);
        return myAgePolicy.calculateFare(fare);
    }
}

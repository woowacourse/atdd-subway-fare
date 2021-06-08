package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import wooteco.subway.member.domain.Member;
import wooteco.subway.validator.SubwayPassword;

public class MemberRequest {

    @Email
    private String email;
    @SubwayPassword(min = 8, max = 14)
    private String password;
    @Positive
    @NotNull
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}

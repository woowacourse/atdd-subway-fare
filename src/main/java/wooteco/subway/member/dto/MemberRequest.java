package wooteco.subway.member.dto;

import javax.validation.constraints.NotNull;
import wooteco.subway.member.domain.Member;

public class MemberRequest {

    @NotNull(message = "INVALID_INPUT")
    private String email;
    @NotNull(message = "INVALID_INPUT")
    private String password;
    @NotNull(message = "INVALID_INPUT")
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

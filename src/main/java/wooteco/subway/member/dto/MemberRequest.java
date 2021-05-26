package wooteco.subway.member.dto;

import org.hibernate.validator.constraints.Length;
import wooteco.subway.member.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.Positive;

public class MemberRequest {

    @Email(message = "이메일이 형식에 알맞지 않습니다.")
    private String email;
    @Length(min = 4, max = 20, message = "비밀번호는 4자 이상 20자 이하여야합니다.")
    private String password;
    @Positive(message = "나이는 양수만 가능합니다.")
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

package wooteco.subway.member.dto;

import javax.validation.constraints.Email;

public class MemberEmailRequest {
    @Email(message = "올바른 이메일 형식으로 입력해주세요")
    private String email;

    MemberEmailRequest() {
    }

    public MemberEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

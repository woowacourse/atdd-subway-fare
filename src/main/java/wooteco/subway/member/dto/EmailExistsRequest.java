package wooteco.subway.member.dto;

import javax.validation.constraints.Email;

public class EmailExistsRequest {

    @Email(message = "올바른 이메일 형식으로 입력해주세요")
    private String email;

    public EmailExistsRequest() {
    }

    public EmailExistsRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

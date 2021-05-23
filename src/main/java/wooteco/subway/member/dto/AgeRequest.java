package wooteco.subway.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AgeRequest {
    @NotNull
    private Integer age;

    public AgeRequest() {
    }

    public AgeRequest(int age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}

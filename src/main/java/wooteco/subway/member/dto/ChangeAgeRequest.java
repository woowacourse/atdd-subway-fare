package wooteco.subway.member.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class ChangeAgeRequest {

    @Min(0) @Max(100)
    private int age;

    public ChangeAgeRequest() {
    }

    public ChangeAgeRequest(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

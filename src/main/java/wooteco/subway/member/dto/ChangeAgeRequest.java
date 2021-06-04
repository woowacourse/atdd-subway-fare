package wooteco.subway.member.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ChangeAgeRequest {

    @Min(value = 0, message = "1부터 150 사이의 나이를 입력해주세요")
    @Max(value = 150, message = "1부터 150 사이의 나이를 입력해주세요")
    private Integer age;

    public ChangeAgeRequest() {
    }

    public ChangeAgeRequest(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

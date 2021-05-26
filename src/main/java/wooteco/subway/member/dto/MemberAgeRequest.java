package wooteco.subway.member.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberAgeRequest {
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @Min(value = 1, message = "1부터 150 사이의 나이를 입력해주세요")
    @Max(value = 150, message = "1부터 150 사이의 나이를 입력해주세요")
    private int age;

    public MemberAgeRequest() {
    }

    public MemberAgeRequest(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}

package wooteco.subway.member.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MemberInfoUpdateRequest {
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @Min(value = 1, message = "1부터 150 사이의 나이를 입력해주세요")
    @Max(value = 150, message = "1부터 150 사이의 나이를 입력해주세요")
    private Integer age;

    public MemberInfoUpdateRequest() {
    }

    public MemberInfoUpdateRequest(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }
}

package wooteco.subway.member.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberAgeRequest {
    @NotNull
    @Positive
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

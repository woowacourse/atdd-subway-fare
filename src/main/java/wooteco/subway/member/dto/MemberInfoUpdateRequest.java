package wooteco.subway.member.dto;

public class MemberInfoUpdateRequest {
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

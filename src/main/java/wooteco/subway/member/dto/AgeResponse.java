package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

public class AgeResponse {
    private Long id;
    private int age;

    public AgeResponse(Long id, int age) {
        this.id = id;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public static AgeResponse of(Member member) {
        return new AgeResponse(member.getId(), member.getAge());
    }
}

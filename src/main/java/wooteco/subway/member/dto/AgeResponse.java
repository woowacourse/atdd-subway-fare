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
}

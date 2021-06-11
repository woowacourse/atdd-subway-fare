package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

public class MemberAgeResponse {
    private Long id;
    private int age;

    public MemberAgeResponse() {
    }

    public MemberAgeResponse(Long id, int age) {
        this.id = id;
        this.age = age;
    }

    public static MemberAgeResponse of(Member member) {
        return new MemberAgeResponse(member.getId(), member.getAge());
    }

    public Long getId() {
        return id;
    }

    public int getAge() {
        return age;
    }
}

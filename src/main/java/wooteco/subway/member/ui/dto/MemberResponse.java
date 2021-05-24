package wooteco.subway.member.ui.dto;

import wooteco.subway.member.domain.Member;

import java.beans.ConstructorProperties;

public class MemberResponse {

    private final Long id;
    private final String email;
    private final Integer age;

    @ConstructorProperties({"id", "email", "age"})
    public MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(Member member) {
        return new MemberResponse(member.getId(), member.getEmail(), member.getAge());
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

}

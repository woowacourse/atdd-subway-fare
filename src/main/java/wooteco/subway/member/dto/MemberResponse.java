package wooteco.subway.member.dto;

import wooteco.subway.member.domain.AuthorizationMember;

public class MemberResponse {
    private Long id;
    private String email;
    private Integer age;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static MemberResponse of(AuthorizationMember authorizationMember) {
        return new MemberResponse(authorizationMember.getId(), authorizationMember.getEmail(), authorizationMember.getAge());
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

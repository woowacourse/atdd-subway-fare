package wooteco.subway.member.domain;

public class LoginMember {

    private static final LoginMember GUEST = new LoginMember(null, null, 20);

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(Member member) {
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }

    public static LoginMember ofGuest() {
        return GUEST;
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

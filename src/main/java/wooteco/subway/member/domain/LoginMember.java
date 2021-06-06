package wooteco.subway.member.domain;

public class LoginMember {

    private static final LoginMember GUEST = new LoginMember(null, "GUEST", 20);

    private final Long id;
    private final String email;
    private final Integer age;

    public LoginMember(Member member) {
        this(member.getId(), member.getEmail(), member.getAge());
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember obtainGuest() {
        return GUEST;
    }

    public boolean isGuest() {
        return this.equals(GUEST);
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

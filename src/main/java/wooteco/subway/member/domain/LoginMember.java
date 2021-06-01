package wooteco.subway.member.domain;

public class LoginMember {

    private static final LoginMember EMPTY_LOGIN_MEMBER = new LoginMember(null, null, null);

    private final Long id;
    private final String email;
    private final Integer age;

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember empty() {
        return EMPTY_LOGIN_MEMBER;
    }

    public boolean isPresent() {
        return !this.equals(EMPTY_LOGIN_MEMBER);
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

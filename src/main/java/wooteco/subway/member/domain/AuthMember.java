package wooteco.subway.member.domain;

public abstract class AuthMember {

    private Long id;
    private String email;
    private Integer age;

    public AuthMember() {}

    protected AuthMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public abstract boolean isLoggedIn();
}

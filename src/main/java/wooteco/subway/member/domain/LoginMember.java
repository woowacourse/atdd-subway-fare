package wooteco.subway.member.domain;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;
    private boolean isLogin;

    public LoginMember() {
    }

    public LoginMember(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public LoginMember(Long id, String email, Integer age, boolean isLogin) {
        this.id = id;
        this.email = email;
        this.age = age;
        this.isLogin = isLogin;
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

    public boolean isLogin() {
        return isLogin;
    }
}

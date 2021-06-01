package wooteco.subway.member.domain;

public class LoginMember {

    private Boolean loginStatus;
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Boolean loginStatus) {
        this(loginStatus, null, null, 20);
    }

    public LoginMember(Long id, String email, Integer age) {
        this(true, id, email, age);
    }

    public LoginMember(Boolean loginStatus, Long id, String email, Integer age) {
        this.loginStatus = loginStatus;
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
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

package wooteco.subway.member.domain;

public class LoginUser implements User {
    private Long id;
    private String email;
    private Integer age;

    public LoginUser() {
    }

    public LoginUser(Member member) {
        this(member.getId(), member.getEmail(), member.getAge());
    }

    public LoginUser(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }


    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public boolean isLogin() {
        return true;
    }
}

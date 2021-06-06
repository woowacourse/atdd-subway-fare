package wooteco.subway.auth.domain;

public class LoginUser extends User {
    public LoginUser(Long id, String email, Integer age) {
        super(id, email, age);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Integer getAge() {
        return age;
    }
}

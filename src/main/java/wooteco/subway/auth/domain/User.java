package wooteco.subway.auth.domain;

public abstract class User {
    protected Long id;
    protected String email;
    protected Integer age;

    public User(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public abstract boolean isLoggedIn();

    public abstract Long getId();

    public abstract String getEmail();

    public abstract Integer getAge();
}

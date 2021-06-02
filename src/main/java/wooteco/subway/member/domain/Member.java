package wooteco.subway.member.domain;

public class Member {

    private final Id id;
    private final Email email;
    private final Password password;
    private final Age age;

    public Member(Long id, String email, Integer age) {
        this(new Id(id), new Email(email), null, new Age(age));
    }

    public Member(String email, String password, Integer age) {
        this(null, new Email(email), new Password(password), new Age(age));
    }

    public Member(Long id, String email, String password, Integer age) {
        this(new Id(id), new Email(email), new Password(password), new Age(age));
    }

    public Member(Id id, Email email, Password password, Age age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Long getId() {
        return id.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password.getValue();
    }

    public Integer getAge() {
        return age.getValue();
    }

    public void checkPassword(String password) {
        this.password.check(password);
    }
}

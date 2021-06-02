package wooteco.subway.member.domain;

public class LoginMember {
    private static final LoginMember anonymous = new LoginMember();

    private Long id;
    private String email;
    private int age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember anonymous() {
        return anonymous;
    }

    public boolean isAnonymous() {
        return anonymous.equals(this);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }
}

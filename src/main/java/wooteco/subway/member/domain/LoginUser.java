package wooteco.subway.member.domain;

public class LoginUser implements User{

    private final long id;
    private final String email;
    private final int age;

    public LoginUser(long id, String email, int age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    @Override
    public boolean isGuest() {
        return false;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public int getAge() {
        return age;
    }


}

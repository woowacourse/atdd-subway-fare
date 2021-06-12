package wooteco.subway.member.domain;

public class NotLoginMember extends AuthMember {

    public static final int DEFAULT_AGE = 20;

    public NotLoginMember() {
        super(null, null, DEFAULT_AGE);
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}

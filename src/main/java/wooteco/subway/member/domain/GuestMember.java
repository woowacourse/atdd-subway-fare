package wooteco.subway.member.domain;

public class GuestMember extends User {
    public GuestMember() {
        super(0L, "guest@gmail.com", 20);
    }

    @Override
    public boolean isLoginMember() {
        return false;
    }
}

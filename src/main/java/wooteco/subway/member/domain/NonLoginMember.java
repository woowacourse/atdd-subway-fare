package wooteco.subway.member.domain;

public class NonLoginMember implements AuthMember {

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public Long getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getEmail() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer getAge() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDiscountedFare(int baseFare) {
        return baseFare;
    }
}

package wooteco.subway.member.domain;

public class GuestMember implements Member {
    @Override
    public Long getId() {
        throw new UnsupportedOperationException("게스트 멤버 입니다.");
    }

    @Override
    public String getEmail() {
        throw new UnsupportedOperationException("게스트 멤버 입니다.");
    }

    @Override
    public Integer getAge() {
        throw new UnsupportedOperationException("게스트 멤버 입니다.");
    }

    @Override
    public int calculateFare(int fare) {
        return fare;
    }
}

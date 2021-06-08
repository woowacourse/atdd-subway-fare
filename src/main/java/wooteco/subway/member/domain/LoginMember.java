package wooteco.subway.member.domain;

public class LoginMember {

    private Long id;
    private Authority authority;

    public LoginMember() {
    }

    public LoginMember(Authority authority) {
        this(null, authority);
    }

    public LoginMember(Long id, Authority authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public Authority getAuthority() {
        return authority;
    }

    public boolean isMember() {
        return authority.equals(Authority.MEMBER);
    }
}

package wooteco.auth.domain;

public class LoginMember {
    private Long id;
    private Authority authority;

    public LoginMember() {
    }

    public LoginMember(Long id) {
        this.id = id;
    }

    public LoginMember(Long id, Authority authority) {
        this.id = id;
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }
}

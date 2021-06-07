package wooteco.auth.domain;

public class LoginMember {

    private static final LoginMember anonymous = new LoginMember(null, Role.ANONYMOUS);

    private Long id;
    private Role role;

    public LoginMember() {
    }

    public LoginMember(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public static LoginMember anonymous() {
        return anonymous;
    }

    public Long getId() {
        return id;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAnonymous() {
        return role.isAnonymous();
    }
}

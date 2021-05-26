package wooteco.auth.domain;

public class LoginMember {
    private Long id;
    private Role role;

    public LoginMember() {
    }

    public LoginMember(Long id, Role role) {
        this.id = id;
        this.role = role;
    }

    public static LoginMember anonymous() {
        return new LoginMember(null, Role.ANONYMOUS);
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

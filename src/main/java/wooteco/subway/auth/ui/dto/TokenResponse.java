package wooteco.subway.auth.ui.dto;

import java.beans.ConstructorProperties;

public class TokenResponse {
    private final String accessToken;

    @ConstructorProperties("accessToken")
    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

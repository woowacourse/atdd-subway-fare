package wooteco.subway.acceptance.auth.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.acceptance.auth.application.AuthService;
import wooteco.subway.acceptance.auth.dto.TokenRequest;
import wooteco.subway.acceptance.auth.dto.TokenResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> login(@RequestBody TokenRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok().body(token);
    }
}

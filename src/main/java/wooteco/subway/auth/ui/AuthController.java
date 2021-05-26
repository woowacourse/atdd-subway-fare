package wooteco.subway.auth.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.auth.application.AuthService;
import wooteco.subway.auth.dto.TokenRequest;
import wooteco.subway.auth.dto.TokenResponse;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/login/token")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody TokenRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok().body(token);
    }
}

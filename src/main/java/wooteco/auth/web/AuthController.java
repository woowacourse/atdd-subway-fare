package wooteco.auth.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wooteco.auth.service.AuthService;
import wooteco.auth.web.dto.request.TokenRequest;
import wooteco.auth.web.dto.response.TokenResponse;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/login/token")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid TokenRequest request) {
        TokenResponse token = authService.login(request);
        return ResponseEntity.ok().body(token);
    }
}

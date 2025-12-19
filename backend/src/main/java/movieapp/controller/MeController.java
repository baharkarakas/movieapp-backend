package movieapp.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

    @GetMapping("/api/me")
    public Map<String, Object> me(Authentication authentication) {
        // authentication.getName() will be the email (subject in JWT)
        return Map.of(
                "email", authentication.getName(),
                "authenticated", authentication.isAuthenticated()
        );
    }
}

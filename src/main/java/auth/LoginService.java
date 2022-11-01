package auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private AuthRepository authRepository;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(AuthRepository authRepository, JwtTokenProvider jwtTokenProvider) {
        this.authRepository = authRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Auth auth = authRepository.findByUsername(tokenRequest.getUsername());
        if (auth == null || auth.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(auth.getPrincipal() + "", auth.getRole());

        return new TokenResponse(accessToken);
    }
}

package auth;

import org.springframework.stereotype.Service;

// TODO: nextstep.member 의존성 제거
@Service
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;

    public LoginService(JwtTokenProvider jwtTokenProvider, AuthenticationProvider authenticationProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationProvider = authenticationProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetail member = authenticationProvider.findByUsername(tokenRequest.getUsername());
        if (member == null || member.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(member.getId() + "", member.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetail extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return authenticationProvider.findById(id);
    }
}

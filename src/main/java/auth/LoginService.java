package auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private UserRepresentationService userRepresentationService;
    private JwtTokenProvider jwtTokenProvider;

    public LoginService(UserRepresentationService userRepresentationService, JwtTokenProvider jwtTokenProvider) {
        this.userRepresentationService = userRepresentationService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetail userDetail = userRepresentationService.findByUsername(tokenRequest.getUsername());
        if (userDetail == null || userDetail.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException();
        }

        String accessToken = jwtTokenProvider.createToken(userDetail.getId() + "", userDetail.getRole());

        return new TokenResponse(accessToken);
    }

    public Long extractPrincipal(String credential) {
        return Long.parseLong(jwtTokenProvider.getPrincipal(credential));
    }

    public UserDetail extractMember(String credential) {
        Long id = Long.parseLong(jwtTokenProvider.getPrincipal(credential));
        return userRepresentationService.findById(id);
    }
}

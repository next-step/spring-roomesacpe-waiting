package auth;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private static final String TOKEN_CLAIM_NAME = "MEMBER";

    private final AuthPrincipal authPrincipal;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(AuthPrincipal authPrincipal, JwtTokenProvider jwtTokenProvider) {
        this.authPrincipal = authPrincipal;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        authPrincipal.checkTokenRequest(tokenRequest);
        UserDetail userDetail = authPrincipal.getUserDetail(tokenRequest.getUsername());

        String accessToken = jwtTokenProvider.createTokenWithClaim(TOKEN_CLAIM_NAME, userDetail);
        return new TokenResponse(accessToken);
    }

    public UserDetail getLoginInfo(String credential) {
        if (jwtTokenProvider.isTokenAlive(credential)) {
            return jwtTokenProvider.getClaim(credential, TOKEN_CLAIM_NAME, UserDetail.class);
        }
        throw new AuthenticationException();
    }
}

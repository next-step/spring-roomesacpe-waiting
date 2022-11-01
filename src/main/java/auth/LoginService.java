package auth;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final UserDetailsRepository userDetailsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(UserDetailsRepository userDetailsRepository, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsRepository = userDetailsRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        UserDetails userDetails = findMemberByUsername(tokenRequest.getUsername());
        if (userDetails.checkWrongPassword(tokenRequest.getPassword())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        String token = jwtTokenProvider.createToken(userDetails.getId().toString(), userDetails.getRole());
        return new TokenResponse(token);
    }

    private UserDetails findMemberByUsername(String username) {
        try {
            return userDetailsRepository.findByUsername(username);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthenticationException("존재하지 않는 계정입니다.");
        }
    }
}

package nextstep.config;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import auth.UserRepresentationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService(UserRepresentationService userRepresentationService, JwtTokenProvider jwtTokenProvider) {
        return new LoginService(userRepresentationService, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}

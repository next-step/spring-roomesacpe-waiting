package nextstep;

import auth.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthConfiguration {

    @Bean
    public JwtTokenProvider jwtTokenProvider(AuthProperties authProperties) {
        return new JwtTokenProvider(authProperties);
    }

    @Bean
    public LoginService loginService(AuthRepository authRepository, JwtTokenProvider jwtTokenProvider) {
        return new LoginService(authRepository, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}

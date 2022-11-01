package roomescape;

import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginService;
import roomescape.member.UserDetailsDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfiguration implements WebMvcConfigurer {
    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService(UserDetailsDao userDetailsDao, JwtTokenProvider jwtTokenProvider) {
        return new LoginService(userDetailsDao, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController(LoginService loginService) {
        return new LoginController(loginService);
    }
}

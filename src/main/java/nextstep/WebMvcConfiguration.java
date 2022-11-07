package nextstep;

import auth.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final AuthPrincipal authPrincipal;

    public WebMvcConfiguration(ObjectMapper objectMapper, AuthPrincipal authPrincipal) {
        this.objectMapper = objectMapper;
        this.authPrincipal = authPrincipal;
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(objectMapper);
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(authPrincipal, jwtTokenProvider());
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        AdminInterceptor interceptor = new AdminInterceptor(loginService());
        registry.addInterceptor(interceptor).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver(loginService()));
    }
}

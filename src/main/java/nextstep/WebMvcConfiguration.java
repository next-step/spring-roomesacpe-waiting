package nextstep;

import auth.AdminInterceptor;
import auth.JwtTokenProvider;
import auth.LoginController;
import auth.LoginMemberArgumentResolver;
import auth.LoginService;
import java.util.List;
import nextstep.member.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final MemberService memberService;

    public WebMvcConfiguration(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor(jwtTokenProvider())).addPathPatterns("/admin/**");
    }

    @Override
    public void addArgumentResolvers(List argumentResolvers) {
        argumentResolvers.add(new LoginMemberArgumentResolver(loginService(jwtTokenProvider())));
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public LoginService loginService(JwtTokenProvider jwtTokenProvider) {
        return new LoginService(memberService, jwtTokenProvider);
    }

    @Bean
    public LoginController loginController() {
        return new LoginController(loginService(jwtTokenProvider()));
    }
}

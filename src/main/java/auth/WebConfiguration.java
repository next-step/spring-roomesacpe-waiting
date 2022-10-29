package auth;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

  private LoginService loginService;
  private JwtTokenProvider jwtTokenProvider;

  public WebConfiguration(LoginService loginService, JwtTokenProvider jwtTokenProvider) {
    this.loginService = loginService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AdminInterceptor(jwtTokenProvider)).addPathPatterns("/admin/**");
  }

  @Override
  public void addArgumentResolvers(List argumentResolvers) {
    argumentResolvers.add(new LoginMemberArgumentResolver(loginService));
  }
}

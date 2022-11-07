package auth;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final LoginService loginService;

    public LoginMemberArgumentResolver(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        try {
            String credential = parseTokenCredential(webRequest);
            UserDetail userDetail = loginService.extractMember(credential);
            validateIfAdmin(parameter, userDetail);
            return userDetail;
        } catch (Exception e) {
            return null;
        }
    }

    private String parseTokenCredential(NativeWebRequest webRequest) {
        String authorization = Objects.requireNonNull(webRequest.getHeader(HttpHeaders.AUTHORIZATION));
        return authorization.split(" ")[1];
    }

    private void validateIfAdmin(MethodParameter parameter, UserDetail userDetail) {
        LoginMember annotation = Objects.requireNonNull(parameter.getParameterAnnotation(LoginMember.class));
        if (annotation.isAdmin() && userDetail.isAdmin()) {
            return;
        }
        throw new AuthenticationException();
    }
}

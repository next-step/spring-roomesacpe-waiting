package nextstep.common;

public class AuthException extends BusinessException{
    private static final String MESSAGE = "권한이 없습니다.";
    public AuthException() {
        super(MESSAGE);
    }
}

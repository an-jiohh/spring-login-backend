package jiohh.springlogin.user.exception;

public class SessionInvalidationException extends RuntimeException {
    public SessionInvalidationException(String message) {
        super(message);
    }

    public SessionInvalidationException() {
        super("인증이 필요합니다");
    }
}

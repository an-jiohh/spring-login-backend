package jiohh.springlogin.user.exception;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException(String message) {
        super(message);
    }
    public SessionExpiredException() {
      super("세션이 만료되었습니다.");
    }
}

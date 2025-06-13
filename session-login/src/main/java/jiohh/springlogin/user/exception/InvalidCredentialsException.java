package jiohh.springlogin.user.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }

    public InvalidCredentialsException() {
        super("아이디 및 비밀번호가 올바르지 않습니다.");
    }
}
